package lavagames.towers.data

class Player(var name: String, var score: Int = 0, var health: Int = 10, var hits: Int = 0, var alive: Boolean = true) {
  lateinit var control: Control
  val pad: Pad by lazy { control.pad }
  val connected: Boolean = control.connected
}