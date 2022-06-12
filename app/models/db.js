const dbConfig = require('../config/config.js');
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
        host: dbConfig.HOST,
        user: dbConfig.USER,
        password: dbConfig.PASSWORD,
        database: dbConfig.database,
        port: dbConfig.port
});

class db{
        async sql_get(sql){
                const conn = await pool.getConnection(async conn => conn);
                try{
                        const rows = await conn.query(sql);
                        return rows;
                } catch (err) {
                        console.log(`\t[DB.js]: Error in exec_sql_get()\n${err}`);
                        return null;
                } finally {
                        conn.release();
                }
        }
        async sql_get_val(sql, values){
                const conn = await pool.getConnection(async conn => conn);
                try{
                        const rows = await conn.query(sql, values);
                        return rows;
                } catch (err) {
                        console.log(`\t[DB.js]: Error in exec_sql_get()\n${err}`);
                        return null;
                } finally {
                        conn.release();
                }
        }
        async sql_ins(sql, values){
                var conn;
                try{
                        conn = await pool.getConnection(async conn => conn);
                        try{
                                conn.query(sql, values)
                        } catch(err){
                                console.log(err);
                                console.log(`\n\n${sql}\n\n`)
                        }
                } catch(err){
                        console.log("\tDB Connection Error");
                } finally{
                        conn.release();
                }
        }
}

module.exports = new db();
