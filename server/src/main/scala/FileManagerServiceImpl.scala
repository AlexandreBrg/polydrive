package fr.dopolytech.polydrive

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import grpc.{FileManagerService, FileRequest, FileResponse}

import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{
  ClusterSharding,
  Entity,
  EntityRef,
  EntityTypeKey
}

import scala.concurrent.Future

class FileManagerServiceImpl(system: ActorSystem[_])
    extends FileManagerService {
  private implicit val sys: ActorSystem[_] = system

  override def fileEvent(in: FileRequest): Future[FileResponse] = {
    val sharding = ClusterSharding(system)

    val TypeKey = EntityTypeKey[Counter.Command]("Counter")

    val counterOne: EntityRef[Counter.Command] =
      sharding.entityRefFor(TypeKey, "counter-1")
    counterOne ! Counter.Increment
    counterOne ! Counter.GetValue
    Future.successful(FileResponse())
  }

}
