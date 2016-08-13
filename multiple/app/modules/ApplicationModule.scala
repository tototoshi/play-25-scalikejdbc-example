package modules

import play.api.{ Configuration, Environment }
import play.api.inject.{ Binding, Module }
import scalikejdbc.ConnectionPool

class ApplicationModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[ConnectionPool].qualifiedWith("default").toProvider[DefaultConnectionPoolProvider].eagerly(),
    bind[ConnectionPool].qualifiedWith("secondary").toProvider[SecondaryConnectionPoolProvider].eagerly(),
    bind[ConnectionPoolShutdown].toSelf.eagerly()
  )
}
