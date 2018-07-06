package example

import java.sql.DriverManager

fun main(args: Array<String>) {
    Class.forName("org.postgresql.Driver")
    val conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kotlin-jdbc-insert-returning-id-demo", "freewind", "") // (2)
    conn.use {
        conn.createStatement().use { stmt ->
            with(stmt) {
                executeUpdate("""create table mytbl(id SERIAL primary key, name varchar(255))""");
            }
        }

        conn.createStatement().use { stmt ->
            val rs = stmt.executeQuery("""insert into mytbl(name) values('Hello') returning id;""")
            rs.next()
            val id = rs.getLong("id") // rs.getLong(1)
            println("generated id: $id")
        }
    }

}

fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        try {
            this?.close()
        } catch (e: Exception) {
            println(e.toString())
        }
    }
}