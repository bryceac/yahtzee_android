package me.brycecampbell.yahtzee

fun List<Int>.countSequential(): Int {
    val uniqueValues = this.toSortedSet().toIntArray()

    var sequentialSet = mutableSetOf<Int>()

    for ((index, value) in uniqueValues.withIndex()) {
        if (value != uniqueValues.last()) {
            val nextIndex = index + 1

            if (nextIndex <= uniqueValues.indices.last()) {
                val nextNumber = uniqueValues[nextIndex]

                if (nextNumber != uniqueValues.last() && nextNumber-value == 1) {
                    sequentialSet.add(value)
                    sequentialSet.add(nextNumber)
                }
            }
        } else if (sequentialSet.isNotEmpty()) {
            val largestNumberInSequence = sequentialSet.maxOrNull()!!

            if (value-largestNumberInSequence == 1) {
                sequentialSet.add(value)

            }
        }
    }

    return sequentialSet.count()
}