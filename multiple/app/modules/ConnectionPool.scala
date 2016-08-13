package modules

import javax.inject.{ Inject, Named, Provider, Singleton }

import play.api.Logger
import play.api.inject.ApplicationLifecycle
import scalikejdbc.config.TypesafeConfigReader
import scalikejdbc.{ Commons2ConnectionPoolFactory, ConnectionPool }

import scala.concurrent.{ ExecutionContext, Future }


class DefaultConnectionPoolProvider extends Provider[ConnectionPool] {
  private val logger = Logger(classOf[DefaultConnectionPoolProvider])
  override def get(): ConnectionPool = {
    val jdbc = TypesafeConfigReader.readJDBCSettings()
    logger.info("Preparing connection pool...")
    Commons2ConnectionPoolFactory(jdbc.url, jdbc.user, jdbc.password)
  }
}

class SecondaryConnectionPoolProvider extends Provider[ConnectionPool] {
  private val logger = Logger(classOf[SecondaryConnectionPoolProvider])
  override def get(): ConnectionPool = {
    val jdbc = TypesafeConfigReader.readJDBCSettings('secondary)
    logger.info("Preparing connection pool...")
    Commons2ConnectionPoolFactory(jdbc.url, jdbc.user, jdbc.password)
  }
}

@Singleton
class ConnectionPoolShutdown @Inject() (
  lifecycle: ApplicationLifecycle,
  @Named("default") defaultConnectionPool: ConnectionPool,
  @Named("secondary") secondaryConnectionPool: ConnectionPool,
  executionContext: ExecutionContext
) {
  private val logger = Logger(classOf[ConnectionPoolShutdown])
  lifecycle.addStopHook(() => Future {
    logger.info("Shutdown connection pool")
    defaultConnectionPool.close()
    secondaryConnectionPool.close()
  }(executionContext))
}