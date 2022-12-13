package be.brammeerten

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.min

class Day13Test {

    @Test
    fun `part 1a`() {
        val pairs = readPairs("day13/input.txt")

        isCorrect(pairs[1].first, pairs[1].second)

        Assertions.assertEquals(13, solve(pairs))
    }

    @Test
    fun `test util parseList`() {
        val actual = parseList("[1,[24,[3,[4,[5,6,7]]]],8,9]")
    }

    fun readPairs(file: String): List<Pair<ValueOrList, ValueOrList>> {
        return readFileSplitted(file, "\n\n")
            .map { (p1, p2) -> parseList(p1) to parseList(p2) }
    }

    fun solve(pairs: List<Pair<ValueOrList, ValueOrList>>): Int {
        val results: List<Int> = pairs.mapIndexed { i, p ->
            if (isCorrect(p.first, p.second) == true) i + 1 else 0
        }
        return results.sum()
    }

    fun isCorrect(left: ValueOrList, right: ValueOrList): Boolean? {
        if (left.isNumber && right.isNumber) {
            if (left.getVal() < right.getVal()) return true
            else if (left.getVal() > right.getVal()) return false
            else return null

        } else if (left.isList && right.isList) {
            for (i in 0 until Math.max(left.list.size, right.list.size)) {
                if (i == left.list.size && i != right.list.size) return true
                if (i != left.list.size && i == right.list.size) return false
                if (i == left.list.size && i == right.list.size) continue
                val res = isCorrect(left.list[i], right.list[i])
                if (res == false) return false
                if (res == true) return true
            }
            return null
        } else if (left.isList) {
            val rightList = ValueOrList(true, right.parent)
            rightList.addItem(right.getVal())
            return isCorrect(left, rightList)
        } else {
            val leftList = ValueOrList(true, left.parent)
            leftList.addItem(left.getVal())
            return isCorrect(leftList, right)
        }
    }

    fun parseList(list: String): ValueOrList {
        var toParse = list.drop(1)
        var curr = ValueOrList(true)

        while (toParse.length > 1) {
            // read next token
            var token: String = ""
            if (toParse[0] == '[') token = "["
            else if (toParse[0] == ']') token = "]"
            else if (toParse[0] == ',') token = ","
            else {
                val end = min(if (toParse.indexOf(",") < 0) Int.MAX_VALUE else toParse.indexOf(","), if (toParse.indexOf("]") < 0) Int.MAX_VALUE else toParse.indexOf("]"))
                if (end == Int.MAX_VALUE) throw IllegalStateException("No end character found: $toParse")
                token = toParse.substring(0, end)
            }
            toParse = toParse.drop(token.length)

            // parse token
            if (token == "[") {
                val new = ValueOrList(true, curr)
                curr.addItem(new)
                curr = new
            } else if (token == "]") {
                curr = curr.parent!!
            } else if (token != ",") {
                curr.addItem(token.toInt())
            }
        }

        return curr
    }

}

class ValueOrList(val isList: Boolean, val parent: ValueOrList? = null, val value: Int? = null) {
    val list = mutableListOf<ValueOrList>()
    val isNumber = !isList

    fun addItem(v: ValueOrList) {
        if (!isList) throw IllegalStateException("Not a list")
        list.add(v)
    }

    fun addItem(v: Int) {
        if (!isList) throw IllegalStateException("Not a list")
        list.add(ValueOrList(false, this, v))
    }

    fun getVal(): Int {
        if (isList) throw IllegalStateException("is a list")
        return value!!
    }

    fun allNums(): Boolean {
        if (!isList) throw IllegalStateException("Not a list")
        return list.find { !it.isNumber } == null
    }
}
