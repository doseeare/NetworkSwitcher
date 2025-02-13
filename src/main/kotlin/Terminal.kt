object Terminal {
    fun getNetworkDevices(): List<String> {
        val process = ProcessBuilder("/bin/bash", "-c", "networksetup -listnetworkserviceorder").start()
        val output = process.inputStream.bufferedReader().readText()

        return extractNetworkNames(output)
    }

    fun extractNetworkNames(input: String): List<String> {
        return Regex("""Hardware Port: (.+?), Device:""")
            .findAll(input)
            .map { it.groupValues[1].trim() }
            .toList()
    }

    suspend fun changeNetworkDevicePriority(targetDevice: String, onComplete: suspend (success : Boolean) -> Unit) {
        val otherDevices = getNetworkDevices().filter { it != targetDevice }

        val order = buildString {
            append("\"$targetDevice\" ")
            for (device in otherDevices) {
                append("\"$device\" ")
            }
        }

        try {
            val process = ProcessBuilder("/bin/bash", "-c", "networksetup -ordernetworkservices $order")
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            println("Output: $output")

            val exitCode = process.waitFor()
            onComplete.invoke(exitCode == 0)
        } catch (e: Exception) {
            println("Ошибка при запуске команды: ${e.message}")
        }
    }


}