package be.brammeerten

fun readFile(file: String) =
        {}::class.java.classLoader.getResourceAsStream(file)
                ?.reader()
                ?.readLines()!!

fun readSingleLine(file: String): String {
        val lines = readFile(file)
        if (lines.size != 1)
                throw IllegalStateException("Expected a single line in file $file")
        return lines[0]
}

fun readFileSplitted(file: String, delimitter: String) =
        readAllText(file).split(delimitter)
                .map { it.split("\n") }

fun readAllText(file: String) =
        {}::class.java.classLoader.getResourceAsStream(file)
                ?.reader()
                ?.readText()!!

fun extractRegexGroups(regex: String, text: String): List<String> {
        val matches = Regex(regex).find(text)
        return matches?.groupValues?.drop(1) ?: throw IllegalStateException("Line does not match regex: $text")
}

fun extractRegexGroupsI(regex: String, text: String) =
        extractRegexGroups(regex, text).map { it.toInt() }

fun String.toCharList() = this.toCharArray().toList()

data class Co(val row: Int, val col: Int) {

        operator fun plus(co: Co): Co {
                return Co(row + co.row, col + co.col)
        }

        operator fun minus(co: Co): Co {
                return Co(row - co.row, col - co.col)
        }

        fun min(vararg cos: Co): Co {
                return cos.fold(this) {acc, co -> Co(Math.min(acc.row, co.row), Math.min(acc.col, co.col))}
        }

        fun max(vararg cos: Co): Co {
                return cos.fold(this) {acc, co -> Co(Math.max(acc.row, co.row), Math.max(acc.col, co.col))}
        }

        companion object {
                @JvmField val UP = Co(-1, 0)
                @JvmField val DOWN = Co(1, 0)
                @JvmField val RIGHT = Co(0, 1)
                @JvmField val LEFT = Co(0, -1)
        }
}

data class C(val x: Int, val y: Int) {

        operator fun plus(co: C): C {
                return C(x + co.x, y + co.y)
        }

        operator fun minus(co: C): C {
                return C(x - co.x, y - co.y)
        }

        fun min(vararg cos: C): C {
                return cos.fold(this) {acc, co -> C(Math.min(acc.x, co.x), Math.min(acc.y, co.y))}
        }

        fun max(vararg cos: C): C {
                return cos.fold(this) {acc, co -> C(Math.max(acc.x, co.x), Math.max(acc.y, co.y))}
        }

        companion object {
                @JvmField val UP = C(0, -1)
                @JvmField val DOWN = C(0, 1)
                @JvmField val RIGHT = C(1, 0)
                @JvmField val LEFT = C(-1, 0)
        }
}

data class C3(val x: Int, val y: Int, val z: Int) {
        operator fun plus(co: C3): C3 {
                return C3(x + co.x, y + co.y, z + co.z)
        }

        operator fun minus(co: C3): C3 {
                return C3(x - co.x, y - co.y, z - co.z)
        }

        fun min(vararg cos: C3): C3 {
                return cos.fold(this) {acc, co -> C3(Math.min(acc.x, co.x), Math.min(acc.y, co.y), Math.min(acc.z, co.z))}
        }

        fun max(vararg cos: C3): C3 {
                return cos.fold(this) {acc, co -> C3(Math.max(acc.x, co.x), Math.max(acc.y, co.y), Math.max(acc.z, co.z))}
        }

        override fun toString(): String {
                return "($x, $y, $z)"
        }

        companion object {
                @JvmField val MAX = C3(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
                @JvmField val MIN = C3(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
                @JvmField val RIGHT = C3(1, 0, 0)
                @JvmField val LEFT = C3(-1, 0, 0)
                @JvmField val UP = C3(0, -1, 0)
                @JvmField val DOWN = C3(0, 1, 0)
                @JvmField val FRONT = C3(0, 0, 1)
                @JvmField val BACK = C3(0, 0, -1)
        }
}

fun Char.toAlphabetIndex(): Int {
        if (this in 'a'..'z')
                return this.toByte().toInt() - 'a'.toByte().toInt()
        else if (this in 'A' .. 'Z')
                return this.toByte().toInt() - 'A'.toByte().toInt()
        else
                throw IllegalStateException("Not an alphabetic character: '$this'")
}

fun gcd(a: Int, b: Int): Int {
    var aa = a
    var bb = b

    while (aa != bb) {
        if (aa > bb)
            aa -= bb
        else
            bb -= aa
    }

    return aa
}