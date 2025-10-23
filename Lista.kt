// ==================== LISTAS ====================

// 1) Retorna o último elemento de uma lista
fun <T> last(list: List<T>): T? {
    return list.lastOrNull()
}

// 2) Retorna o penúltimo elemento de uma lista
fun <T> penultimate(list: List<T>): T? {
    return if (list.size >= 2) list[list.size - 2] else null
}

// 3) Verifica se uma lista é palíndroma
fun <T> isPalindrome(list: List<T>): Boolean {
    return list == list.reversed()
}

// 4) Codificação run-length
fun <T> encode(list: List<T>): List<Pair<Int, T>> {
    if (list.isEmpty()) return emptyList()

    val result = mutableListOf<Pair<Int, T>>()
    var current = list[0]
    var count = 1

    for (i in 1 until list.size) {
        if (list[i] == current) {
            count++
        } else {
            result.add(Pair(count, current))
            current = list[i]
            count = 1
        }
    }
    result.add(Pair(count, current))

    return result
}

// 5) Decodificação run-length
fun <T> decode(encoded: List<Pair<Int, T>>): List<T> {
    val result = mutableListOf<T>()
    for ((count, value) in encoded) {
        repeat(count) {
            result.add(value)
        }
    }
    return result
}

// ==================== ARITMÉTICA ====================

// 6) Verifica se um número é primo
fun Int.isPrime(): Boolean {
    if (this < 2) return false
    if (this == 2) return true
    if (this % 2 == 0) return false

    val sqrt = Math.sqrt(this.toDouble()).toInt()
    for (i in 3..sqrt step 2) {
        if (this % i == 0) return false
    }
    return true
}

// 7) Calcula o MDC (Maior Divisor Comum) usando algoritmo de Euclides
fun gcd(a: Int, b: Int): Int {
    var num1 = a
    var num2 = b

    while (num2 != 0) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}

// 8) Retorna lista de números primos em um intervalo
fun listPrimesInRange(range: IntRange): List<Int> {
    return range.filter { it.isPrime() }
}

// ==================== ÁRVORES ====================

// Definição das classes de árvore
interface Tree<out T>

data class Node<out T>(
    val value: T,
    val left: Tree<T> = End,
    val right: Tree<T> = End
) : Tree<T> {
    override fun toString(): String {
        val children = if (left == End && right == End) "" else " $left $right"
        return "T($value$children)"
    }
}

val End = object : Tree<Nothing> {
    override fun toString() = "."
}

// 9) Adiciona nós na árvore binária de busca
fun <T : Comparable<T>> add(tree: Tree<T>, value: T): Tree<T> = when (tree) {
    is Node -> when {
        value < tree.value -> tree.copy(left = add(tree.left, value))
        value > tree.value -> tree.copy(right = add(tree.right, value))
        else -> tree
    }
    End -> Node(value)
    else -> tree
}

// 10) Conta a quantidade de folhas de uma árvore
fun <T> countLeaves(tree: Tree<T>): Int = when (tree) {
    is Node -> {
        if (tree.left == End && tree.right == End) 1
        else countLeaves(tree.left) + countLeaves(tree.right)
    }
    End -> 0
    else -> 0
}

// 11) Retorna os valores das folhas de uma árvore
fun <T> leafValues(tree: Tree<T>): List<T> = when (tree) {
    is Node -> {
        if (tree.left == End && tree.right == End)
            listOf(tree.value)
        else
            leafValues(tree.left) + leafValues(tree.right)
    }
    End -> emptyList()
    else -> emptyList()
}

// 12) Converte árvore para string com notação especial
fun <T> Tree<T>.convertToString(): String = when (this) {
    is Node -> {
        val leftStr = if (left != End) left.convertToString() else ""
        val rightStr = if (right != End) right.convertToString() else ""

        if (left == End && right == End)
            "$value"
        else
            "$value($leftStr,$rightStr)"
    }
    End -> ""
    else -> ""
}

// 13) Converte string para árvore
fun parseTree(str: String): Tree<String> {
    if (str.isBlank()) return End

    fun parseNode(s: String, start: Int = 0): Pair<Tree<String>, Int> {
        val sb = StringBuilder()
        var i = start

        while (i < s.length && s[i] !in listOf('(', ')', ',')) {
            sb.append(s[i])
            i++
        }

        val value = sb.toString().trim()
        if (value.isEmpty()) return End to i

        var left: Tree<String> = End
        var right: Tree<String> = End

        if (i < s.length && s[i] == '(') {
            i++
            val (leftNode, nextIdx1) = parseNode(s, i)
            i = nextIdx1
            if (i < s.length && s[i] == ',') i++
            val (rightNode, nextIdx2) = parseNode(s, i)
            i = nextIdx2
            if (i < s.length && s[i] == ')') i++
            left = leftNode
            right = rightNode
        }

        return Node(value, left, right) to i
    }

    return parseNode(str).first
}

// ==================== TESTES ====================

fun main() {
    println("=== TESTES DE LISTAS ===")
    println("1. Último elemento: ${last(listOf(1, 1, 2, 3, 5, 8))}")
    println("2. Penúltimo elemento: ${penultimate(listOf(1, 1, 2, 3, 5, 8))}")
    println("3. É palíndromo: ${isPalindrome(listOf(1, 2, 3, 2, 1))}")
    println("4. Encode: ${encode("aaaabccaadeeee".toList())}")
    println("5. Decode: ${decode(listOf(Pair(4, 'a'), Pair(1, 'b'), Pair(2, 'c'), Pair(2, 'a'), Pair(1, 'd'), Pair(4, 'e')))}")

    println("\n=== TESTES DE ARITMÉTICA ===")
    println("6. 7 é primo: ${7.isPrime()}")
    println("7. MDC(36, 63): ${gcd(36, 63)}")
    println("8. Primos de 7 a 31: ${listPrimesInRange(7..31)}")

    println("\n=== TESTES DE ÁRVORES ===")
    var tree: Tree<Int> = End
    tree = add(tree, 5)
    tree = add(tree, 3)
    tree = add(tree, 7)
    tree = add(tree, 4)
    tree = add(tree, 6)
    println("9. Árvore binária de busca: $tree")

    val tree1 = Node("x", Node("x"), End)
    println("10. Contagem de folhas: ${countLeaves(tree1)}")

    val tree2 = Node("a", Node("b"), Node("c", Node("d"), Node("e")))
    println("11. Valores das folhas: ${leafValues(tree2)}")

    val tree3 = Node("a", Node("b", Node("d"), Node("e")), Node("c", End, Node("f", Node("g"), End)))
    println("12. Árvore como string: ${tree3.convertToString()}")

    val s = "a(b(d,e),c(,f(g,)))"
    val tree4 = parseTree(s)
    println("13. String para árvore: $tree4")
    println("    Verificação: ${tree4.convertToString()}")
}