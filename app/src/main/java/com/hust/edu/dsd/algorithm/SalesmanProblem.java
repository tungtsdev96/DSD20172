/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hust.edu.dsd.algorithm;

/**
 *
 * @author tungts
 */
public class SalesmanProblem {

    double cmin,fopt,n;
    double can;
    int a[],xopt[];
    double chiphi[][];
    
    boolean chuaXet[];
    
    public SalesmanProblem(int n, double chiphi[][]){
        this.chiphi = chiphi;
        this.n = n;
        chuaXet = new boolean[n];
        a = new int[n]; a[0] = 0;
        xopt = new int[n];
        cmin = Integer.MAX_VALUE;
        fopt = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++){
            chuaXet[i] = true;
            for (int j = 0; j < n; j++){
                if ((i != j) && (cmin > this.chiphi[i][j])) cmin = this.chiphi[i][j];
            }
        }
    }
    
    public void branchAndBound(int i){
        for (int j = 1 ; j < n ; j++){
            if (chuaXet[j]){
                a[i] = j;
                chuaXet[j] = false;
                can = can + chiphi[a[i-1]][a[i]];
                if (i == n-1) {
                    result();
                }
                else {
                    if (can + (n - i)*cmin < fopt) branchAndBound(i+1);
                }
                can -= chiphi[a[i-1]][a[i]];
                chuaXet[j] = true;
            }
        }
    }

    private void result() {
        if (can < fopt) {
            fopt = can;
            for (int j = 0; j < a.length ; j++){
                xopt[j] = a[j];
            }
        }
    }

    public int[] getXopt() {
        return xopt;
    }
}
