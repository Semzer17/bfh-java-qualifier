package com.example.bfhqualifier.solver;

public class SolverFactory {
    public SqlSolver solverFor(String code) {
        return switch (code) {
            case "Q1" -> new Q1Solver();
            case "Q2" -> new Q2Solver();
            default -> new Q1Solver();
        };
    }
}
