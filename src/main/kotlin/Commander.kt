object Commander {
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
        // Получаем все устройства, кроме targetDevice
        val otherDevices = getNetworkDevices().filter { it != targetDevice }

        // Строим строку для команды с добавлением кавычек для каждого устройства
        val order = buildString {
            append("\"$targetDevice\" ")
            for (device in otherDevices) {
                append("\"$device\" ")
            }
        }

        // Запускаем процесс
        try {
            val process = ProcessBuilder("/bin/bash", "-c", "networksetup -ordernetworkservices $order")
                .redirectErrorStream(true)
                .start()

            // Чтение вывода процесса
            val output = process.inputStream.bufferedReader().readText()
            println("Output: $output")

            // Проверка на успешное завершение процесса
            val exitCode = process.waitFor()
            onComplete.invoke(exitCode == 0)
        } catch (e: Exception) {
            println("Ошибка при запуске команды: ${e.message}")
        }
    }


}