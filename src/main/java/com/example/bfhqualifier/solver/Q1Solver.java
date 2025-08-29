package com.example.bfhqualifier.solver;

/**
 * Odd last-two digits → Question 1
 * Final SQL is MySQL-compatible.
 */
public class Q1Solver implements SqlSolver {
    @Override
    public String buildFinalQuery() {
        return """SELECT
    mp.amount AS SALARY,
    CONCAT(e.first_name, ' ', e.last_name) AS NAME,
    TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE,
    d.department_name AS DEPARTMENT_NAME
FROM (
    SELECT p.emp_id, p.amount
    FROM payments p
    WHERE DAY(p.payment_time) <> 1
    ORDER BY p.amount DESC
    LIMIT 1
) AS mp
JOIN employee e ON e.emp_id = mp.emp_id
JOIN department d ON d.department_id = e.department;""";
    }
}
