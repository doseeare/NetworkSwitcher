import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

object DataBase{

    fun readTextFile(): String? {
        val path: Path = "src/main/kotlin/settings.txt".toPath()
        return if (FileSystem.SYSTEM.exists(path)) {
            FileSystem.SYSTEM.read(path) { readUtf8() }
        } else {
            null
        }
    }
    fun writeTextFile(text: String) {
        val path: Path = "src/main/kotlin/settings.txt".toPath()
        FileSystem.SYSTEM.write(path) {
            writeUtf8(text)
        }
    }

}