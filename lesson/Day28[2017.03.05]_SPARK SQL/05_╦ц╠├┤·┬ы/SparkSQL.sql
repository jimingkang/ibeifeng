
-- 设置Hive的本地模式
set hive.exec.mode.local.auto = true ;


-- emp\dept 进行关联连接JOIN
SELECT e.ename, e.sal, d.dname FROM db_hive.emp e JOIN db_hive.dept d ON e.deptno = d.deptno ;

-- spark-shell
sqlContext.sql("SELECT e.ename, e.sal, d.dname FROM db_hive.emp e JOIN db_hive.dept d ON e.deptno = d.deptno").show



-- SQL
sqlContext.sql("SELECT deptno, double_scala(avg(sal), 2) FROM db_hive.emp GROUP BY deptno").show
-- +------+------------------+
-- |deptno|               _c1|
-- +------+------------------+
-- |    10|2916.6666666666665|
-- |    20|            2175.0|
-- |    30|1566.6666666666667|
-- +------+------------------+
-- DSL
sqlContext.read.table("db_hive.emp").groupBy($"deptno").agg("sal" -> "avg").show

-- ======================================================
val emp_df = sqlContext.read.table("db_hive.emp")
-- emp_df: org.apache.spark.sql.DataFrame = [empno: int, ename: string, job: string, mgr: int, hiredate: string, sal: double, comm: double, deptno: int]
val dept_df = sqlContext.read.table("db_hive.dept")
-- dept_df: org.apache.spark.sql.DataFrame = [deptno: int, dname: string, loc: string]
val join_df = emp_df.join(dept_df, "deptno")
-- join_df: org.apache.spark.sql.DataFrame = [deptno: int, empno: int, ename: string, job: string, mgr: int, hiredate: string, sal: double, comm: double, dname: string, loc: string]
join_df.select($"ename", $"sal", $"dname").show

sqlContext.read.table("db_hive.emp").join(sqlContext.read.table("db_hive.dept"), "deptno").select($"ename", $"sal", $"dname").show

-- UDF
sqlContext.udf.register(
  "double_scala",
  (numValue: Double, scale: Int) => {
    import java.math.BigDecimal(numValue)
    val db = new BigDecimal(numValue)
    db.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue()
  }
)


-- 分析函数
--	 ROW_NUMBER
--       从字面意思来看，一个虚拟的数字，给每一行进行一个编号
-- 需求：
--	 获取EMP表中各个部门人员薪资最高的三个人的姓名、薪资和部门名称
-- 此函数的使用场景
--		组内TopKey

USE db_hive ;
SELECT
  t.empno, t.ename, t.sal, t.deptno
FROM(  
  SELECT
    empno, ename, sal, deptno,
    ROW_NUMBER() OVER (PARTITION BY deptno ORDER BY sal DESC) AS num 
  FROM 
    emp
)t	
WHERE t.num <= 3 ;

-- ROW_NUMBER, 编号来说,是从 每个组内开始的，开始值是1
-- +--------+---------+---------+---------+------+--+
-- | empno  |  ename  |   sal   | deptno  | num  |
-- +--------+---------+---------+---------+------+--+
-- | 7934   | MILLER  | 1300.0  | 10      | 1    |
-- | 7782   | CLARK   | 2450.0  | 10      | 2    |
-- | 7839   | KING    | 5000.0  | 10      | 3    |

-- | 7369   | SMITH   | 800.0   | 20      | 1    |
-- | 7876   | ADAMS   | 1100.0  | 20      | 2    |
-- | 7566   | JONES   | 2975.0  | 20      | 3    |
-- | 7788   | SCOTT   | 3000.0  | 20      | 4    |
-- | 7902   | FORD    | 3000.0  | 20      | 5    |

-- | 7900   | JAMES   | 950.0   | 30      | 1    |
-- | 7521   | WARD    | 1250.0  | 30      | 2    |
-- | 7654   | MARTIN  | 1250.0  | 30      | 3    |
-- | 7844   | TURNER  | 1500.0  | 30      | 4    |
-- | 7499   | ALLEN   | 1600.0  | 30      | 5    |
-- | 7698   | BLAKE   | 2850.0  | 30      | 6    |
-- +--------+---------+---------+---------+------+--+
 

-- +--------+---------+---------+---------+--+
-- | empno  |  ename  |   sal   | deptno  |
-- +--------+---------+---------+---------+--+
-- | 7839   | KING    | 5000.0  | 10      |
-- | 7782   | CLARK   | 2450.0  | 10      |
-- | 7934   | MILLER  | 1300.0  | 10      |
-- | 7788   | SCOTT   | 3000.0  | 20      |
-- | 7902   | FORD    | 3000.0  | 20      |
-- | 7566   | JONES   | 2975.0  | 20      |
-- | 7698   | BLAKE   | 2850.0  | 30      |
-- | 7499   | ALLEN   | 1600.0  | 30      |
-- | 7844   | TURNER  | 1500.0  | 30      |
-- +--------+---------+---------+---------+--+






