package lavagames.towers.data

interface Pad {
  val A : Boolean
  val B : Boolean
  val X : Boolean
  val Y : Boolean
  val RB : Boolean
  val LB : Boolean

  val RT : Float
  val LT : Float
  val AxisLeftX : Float
  val AxisLeftY : Float
  val AxisRightX : Float
  val AxisRightY : Float
  val connected : Boolean
}