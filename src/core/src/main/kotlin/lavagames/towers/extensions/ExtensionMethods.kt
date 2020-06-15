package lavagames.towers.extensions

import lavagames.towers.Assets

fun Int.clampAround(minValue: Int = 0, maxValue: Int = 360) : Int {
	if(this >= maxValue)
		return this - maxValue
	if(this < minValue)
		return maxValue - this

	return this
}

fun Float.clampAround(minValue: Float = 0f, maxValue: Float = 360f) : Float {
	if(this >= maxValue)
		return this - maxValue
	if(this < minValue)
		return maxValue - this

	return this
}

fun Float.toAnimKey() : String {
	return when(this) {
		in 225f..314.9f -> Assets.WALKNORTH
		in 315f..359.9f -> Assets.WALKEAST
		in 0f..44.9f -> Assets.WALKEAST
		in 45f..134.9f -> Assets.WALKSOUTH
		in 135f..224.9f -> Assets.WALKWEST
		else -> Assets.WALKWEST
	}
}
