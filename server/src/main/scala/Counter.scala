package fr.dopolytech.polydrive

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Counter {
  sealed trait Command
  case object Increment extends Command
  final case class GetValue(replyTo: ActorRef[Int]) extends Command

  def apply(entityId: String): Behavior[Command] = {
    def updated(value: Int): Behavior[Command] = {
      Behaviors.receiveMessage[Command] {
        case Increment =>
          println("Create value of counter is " + value)
          updated(value + 1)
        case GetValue(replyTo) =>
          println("Get value of counter is " + value)
          replyTo ! value
          Behaviors.same
      }
    }

    updated(0)

  }
}
