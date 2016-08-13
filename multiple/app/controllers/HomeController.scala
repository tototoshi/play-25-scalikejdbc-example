package controllers

import javax.inject._

import play.api.mvc._
import scalikejdbc._

@Singleton
class HomeController @Inject() (
  @Named("default") defaultConnectionPool: ConnectionPool,
  @Named("secondary") secondaryConnectionPool: ConnectionPool
) extends Controller {

  private def db = DB(defaultConnectionPool.borrow())

  private def db2 = DB(secondaryConnectionPool.borrow())

  private def getDatabaseName()(implicit session: DBSession): String =
    sql"CALL DATABASE()".map(rs => rs.string("database()")).single().apply().getOrElse(sys.error("db error"))

  def index = Action {
    val dbname = db.readOnly { implicit session =>
      getDatabaseName()
    }
    Ok(dbname.toString)
  }

  def secondary = Action {
    val dbname = db2.readOnly { implicit session =>
      getDatabaseName()
    }
    Ok(dbname.toString)
  }

}
