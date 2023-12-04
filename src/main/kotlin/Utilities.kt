internal fun read(file: String): List<String> =
    object {}.javaClass.getResource(file)
        ?.readText()
        ?.split("\n")
        ?: emptyList()