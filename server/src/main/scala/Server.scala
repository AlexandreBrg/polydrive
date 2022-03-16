package fr.dopolytech.polydrive

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{
  ClusterSharding,
  Entity,
  EntityTypeKey
}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object Server {
  object RootBehavior {
    def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      context.spawn(ClusterListener(), "ClusterListener")

      Behaviors.empty
    }
  }

  def main(args: Array[String]): Unit = {
    startup()
  }

  def startup(): Unit = {
    val config = ConfigFactory
      .parseString(s"""
        akka.http.server.preview.enable-http2 = on
        """)
      .withFallback(ConfigFactory.load())

    val clusterName = config.getString("clustering.cluster.name")
    val grpcPort = config.getString("clustering.grpc.port").toInt
    val system = ActorSystem[Nothing](RootBehavior(), clusterName, config)
    new FileManager(system).run(grpcPort)
    new Server(system).run()
  }
}

class Server(system: ActorSystem[_]) {
  def run(): Unit = {
    implicit val sys: ActorSystem[_] = system
    implicit val ec: ExecutionContext = system.executionContext

    val sharding = ClusterSharding(system)
    val TypeKey = EntityTypeKey[Counter.Command]("Counter")

    val shardRegion: ActorRef[ShardingEnvelope[Counter.Command]] = {
      sharding.init(
        Entity(TypeKey)(createBehavior =
          entityContext => Counter(entityContext.entityId)
        )
      )
    }
  }
}
