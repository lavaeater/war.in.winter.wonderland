package lavagames.towers.ecs

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import lavagames.towers.data.Player
import lavagames.towers.ecs.components.RemoveComponent

class CollisionListener : ContactListener {

  override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
  }

  override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
  }

  override fun endContact(contact: Contact?) {
  }

  override fun beginContact(contact: Contact) {
    when(contact.whatsHappening()) {
      CollisionType.StaticOnStatic -> return
      CollisionType.DynamicOnStatic -> evaluateDynamicOnStatic(contact)
      CollisionType.DynamicOnDynamic -> evaluateDynamicOnDynamic(contact)
    }
  }

  private fun evaluateDynamicOnDynamic(contact: Contact) {
    //For now, we only care about projectiles hitting players or npc's
    if(contact.isProjectileOnCharacter()) {
      evaluateProjectileHitOnCharacter(contact)
    }
  }

  private fun evaluateProjectileHitOnCharacter(contact: Contact) {
    val projectile = contact.getProjectile()
    //last step, remove entity:
    //removeProjectile(projectile)
  }

  private fun evaluateDynamicOnStatic(contact: Contact) {
    //For now, we only care if the contact is entity -> static
    if(contact.hasProjectile()) {
      removeProjectile(contact.getProjectile())
    }
  }

  fun removeProjectile(projectile: PhysicsBodyType.Projectile) {
    projectile.entity.markForRemoval()
  }

  private fun evaluatePlayerOnStaticContact(player: Player, contact: Contact) {
    //Might come in handy later - if the player touches buildable things etc
  }
}

enum class CollisionType {
  StaticOnStatic,
  DynamicOnStatic,
  DynamicOnDynamic
}

/**
 * Gets the FIRST dynamic body in a Contact.
 *
 * If A is dynamic, it returns A's body, else
 * it returns B. If none are dynamic,
 * it throws an exception.
 */
fun Contact.getDynamicBody() : Body {
  return when {
    fixtureA.body.type == BodyDef.BodyType.DynamicBody -> fixtureA.body
    fixtureB.body.type == BodyDef.BodyType.DynamicBody -> fixtureB.body
    else -> throw IllegalArgumentException("No fixtures were dynamic")
  }
}

/**
 * Gets the FIRST static body in a Contact.
 *
 * If A is static, it returns A's body, else
 * it returns B. If none are static,
 * it throws an exception.
 */
fun Contact.getStaticBody() : Body {
  return when {
    fixtureA.body.type == BodyDef.BodyType.StaticBody -> fixtureA.body
    fixtureB.body.type == BodyDef.BodyType.StaticBody -> fixtureB.body
    else -> throw IllegalArgumentException("No fixtures were static")
  }
}

fun Contact.whatsHappening() : CollisionType {
  return if(fixtureA.body.type == BodyDef.BodyType.DynamicBody &&
        fixtureB.body.type == BodyDef.BodyType.DynamicBody)
    CollisionType.DynamicOnDynamic
  else if(fixtureA.body.type == BodyDef.BodyType.StaticBody &&
      fixtureB.body.type == BodyDef.BodyType.StaticBody)
    CollisionType.StaticOnStatic
  else
    CollisionType.DynamicOnStatic
}

fun Contact.resolve() {

  if(fixtureA.body.type == BodyDef.BodyType.DynamicBody &&
     fixtureB.body.type == BodyDef.BodyType.DynamicBody) {

    if (fixtureA.body.userData is PhysicsBodyType &&
        fixtureB.body.userData is PhysicsBodyType) {
      val character = getCharacter()
      val projectile = getProjectile()
    }
  }
}

fun Entity.markForRemoval() {
  add(RemoveComponent())
}

sealed class PhysicsBodyType {
  data class Projectile(val entity: Entity) : PhysicsBodyType()
  data class Character(val character: Entity) : PhysicsBodyType()
}

fun Contact.getCharacter() : PhysicsBodyType.Character {
  return when {
    fixtureA.body.userData is PhysicsBodyType.Character -> fixtureA.body.userData as PhysicsBodyType.Character
    fixtureB.body.userData is PhysicsBodyType.Character -> fixtureB.body.userData as PhysicsBodyType.Character
    else -> throw IllegalArgumentException("No body was a Character")
  }
}

fun Contact.getProjectile() : PhysicsBodyType.Projectile {
  return when {
    fixtureA.body.userData is PhysicsBodyType.Projectile -> fixtureA.body.userData as PhysicsBodyType.Projectile
    fixtureB.body.userData is PhysicsBodyType.Projectile -> fixtureB.body.userData as PhysicsBodyType.Projectile
    else -> throw IllegalArgumentException("No body was a Projectile")
  }
}

fun Contact.hasProjectile() : Boolean {
  return fixtureA.body.userData is PhysicsBodyType.Projectile || fixtureB.body.userData is PhysicsBodyType.Projectile
}

fun Contact.isCharacter() : Boolean {
  return fixtureA.body.userData is PhysicsBodyType.Character || fixtureB.body.userData is PhysicsBodyType.Character
}

fun Contact.isProjectileOnCharacter() : Boolean {
  return isCharacter() && hasProjectile()
}