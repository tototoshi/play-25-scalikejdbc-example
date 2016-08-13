package controllers

import javax.inject._

import play.api.mvc._
import scalikejdbc._

@Singleton
class HomeController @Inject() (connectionPool: ConnectionPool) extends Controller {

  private def db = DB(connectionPool.borrow())

  private def getDatabaseName()(implicit session: DBSession): String =
    sql"CALL DATABASE()".map(rs => rs.string("database()")).single().apply().getOrElse(sys.error("db error"))

  def index = Action {
    val dbname = db.readOnly { implicit session =>
      getDatabaseName()
    }
    Ok(dbname.toString)
  }

}
