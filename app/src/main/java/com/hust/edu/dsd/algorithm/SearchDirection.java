/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hust.edu.dsd.algorithm;

import android.util.Log;

import com.hust.edu.dsd.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author tungts
 */
public class SearchDirection {

    private int[][] Matrix = new int[50][50]; //danh dau trong thai cac nut

    ArrayList<ToaDo> OPEN = new ArrayList<>();
    ArrayList<ToaDo> CLOSE = new ArrayList<>();

    private HashMap<ToaDo, ToaDo> mapToadocha;     //mảng chưa toạ độ cha của nút. có giá trị: x*n +y (x, y là toạ độ của cha)

    private ToaDo diemDau;
    private ToaDo diemCuoi;

    private ToaDo hienTai = new ToaDo();
    private ToaDo dangDuyet = new ToaDo();

    private ArrayList<Huongxet> huongxet = new ArrayList(); //lưu 8 vị trí xung quanh
    private int[] hoanhdohuongxet = {1, 0, -1, -1, -1, 0, 1, 1}; //biên độ của toạ độ các nút xung quanh
    private int[] tungdohuongxet = {1, 1, 1, 0, -1, -1, -1, 0};

    public SearchDirection(int xDau, int yDau, int xCuoi, int yCuoi, int Matrix[][]) {
        duongDi = new ArrayList<>();
        diemDau = new ToaDo();
        for (int i = 0 ; i < 50 ; i++){
            for (int j = 0; j < 50 ; j++ ){
                this.Matrix[i][j] = Matrix[i][j];
            }
        }
        diemDau.x = xDau;
        diemDau.y = yDau;
        diemCuoi = new ToaDo();
        diemCuoi.x = xCuoi;
        diemCuoi.y = yCuoi;

        for (int i = 0; i < 8; i++) {   //khởi tạo biên độ các nút xung quanh
            Huongxet a = new Huongxet();
            a.x = this.hoanhdohuongxet[i];
            a.y = this.tungdohuongxet[i];
            huongxet.add(a);
        }
        
    }

    public void searchDirection() {

        //khoi tao
        mapToadocha = new HashMap<>();

        //chèn nút khởi đầu vào hàng đợi
        OPEN = new ArrayList<>();
        OPEN.add(diemDau);

        // Đối với mỗi nút, tổng chi phí nhận được từ nút bắt đầu đến mục tiêu
        // bằng cách đi qua nút đó. Giá trị đó được biết một phần, một phần là heuristic.
        //vong lap tim duong
        double chiPhiDiDuoc = 0; //gn

        while (!OPEN.isEmpty()) {

            //LẤY NÚT RA TỪ HÀNG ĐỢI
            hienTai = OPEN.get(0);
            chiPhiDiDuoc = OPEN.get(0).gn;

            //danh dau nut nay da duyet
            diemDau.daDuyet = true;
            Matrix[diemDau.x][diemDau.y] = 99;
            CLOSE.add(diemDau);
            OPEN.remove(0);

            //duyet cac o ke vs no
            for (int i = 0; i < 8; i++) {
                dangDuyet = new ToaDo();
                dangDuyet.x = hienTai.x + huongxet.get(i).x;
                dangDuyet.y = hienTai.y + huongxet.get(i).y;

                //NẾU LÀ ĐIỂM ĐÍCH THÌ DỪNG CHƯƠNG TRÌNH
                if (dangDuyet.x == diemCuoi.x && dangDuyet.y == diemCuoi.y) {
                    //stop, hien thi duong di
                    mapToadocha.put(dangDuyet, hienTai);
                    hienThiDuongDi(chiPhiDiDuoc,i);
                    return;
                }
                
                //KIỂM TRA - NẾU ĐÃ DUYỆT (K PHÙ HỢP): BỎ QUA
                // nếu đã duyệt, tường thì bỏ qua, qua pvi ban do 
                if (dangDuyet.x < 0 || dangDuyet.x > 49 || dangDuyet.y < 0 || dangDuyet.y > 49
                        || Matrix[dangDuyet.x][dangDuyet.y] == 99
                        || Matrix[dangDuyet.x][dangDuyet.y] == Constants.WATER_SOURCE
                        || Matrix[dangDuyet.x][dangDuyet.y] == Constants.TREES
                        || Matrix[dangDuyet.x][dangDuyet.y] == Constants.TREE_IRRIGATED
                        || Matrix[dangDuyet.x][dangDuyet.y] == Constants.TREE_REGISTRATION
                        || Matrix[dangDuyet.x][dangDuyet.y] == Constants.BUILDING) {
                    continue;
                }

                //KIỂM TRA ĐÃ NẰM TRONG HÀNG ĐỢI OPEN CHƯA, NẾU CHƯA THÌ THIẾT LẬP GIÁ TRỊ VÀ LƯU VÀO
                if (!kiemTraNodeNamTrongHangDoi(chiPhiDiDuoc)) {
                    int a = Math.abs(diemCuoi.x - dangDuyet.x);
                    int b = Math.abs(diemCuoi.y - dangDuyet.y);

                    dangDuyet.hn = (double) Math.sqrt(a * a + b * b);
                    if (i % 2 == 1) {
                        dangDuyet.gn = chiPhiDiDuoc + 1;
                    } else {
                        dangDuyet.gn = chiPhiDiDuoc + Math.sqrt(2);
                    }
                    dangDuyet.fn = dangDuyet.gn + dangDuyet.hn;
                    chenVaoHangDoi(dangDuyet);
                }

            }

        } //làm đến khi nào OPEN hết(k tìm thấy) hoặc đến đích    

    }

    private void chenVaoHangDoi(ToaDo dangDuyet) {
        int vitrichen = 0;
        if (!OPEN.isEmpty()) {
            int m;
            for (m = 0; m < OPEN.size(); m++) {
                if (OPEN.get(m).fn >= dangDuyet.fn) {
                    break;
                }
            }
            vitrichen = m;

        }
        OPEN.add(vitrichen, dangDuyet);
    }

    public boolean kiemTraNodeNamTrongHangDoi(double chiPhiDiDuoc) {
        ToaDo toaDoCha = mapToadocha.get(dangDuyet);
        if (toaDoCha != null) {

            int chiso = -1;
            //tìm toạ độ nút cha, nằm ở hàng đợi CLOSE
            for (int j = 0; j < CLOSE.size(); j++) {      //tìm
                if (CLOSE.get(j).x == toaDoCha.x && CLOSE.get(j).y == toaDoCha.y) {
                    chiso = j;
                    break;
                }
            }
            
            //so sánh xem gn mới với gn cũ cái nào nhỏ hơn thì cho toạ độ cha theo đường đấy
            if ((CLOSE.get(chiso).gn + this.Khoangcach(toaDoCha.x, toaDoCha.y, dangDuyet.x, dangDuyet.y))
                    > (chiPhiDiDuoc + this.Khoangcach(hienTai.x, hienTai.y, dangDuyet.x, dangDuyet.y))) {
                mapToadocha.put(dangDuyet, hienTai);
                return true;
            }
        }
        mapToadocha.put(dangDuyet, hienTai);
        return false;
    }

    private double Khoangcach(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y2 - y1) * (y2 - y1));
    }

    private void hienThiDuongDi(double chiPhi,int i) {
        chiPhi += (hienTai.x == (dangDuyet.x - huongxet.get(i).x) || hienTai.y == (dangDuyet.y - huongxet.get(i).y)) ? 1 : (float) Math.sqrt(2);
        ArrayList<ToaDo> duongDi = new ArrayList<>();
        duongDi.add(dangDuyet);
        do {            
            ToaDo toaDo = mapToadocha.get(dangDuyet);
            duongDi.add(toaDo);
            dangDuyet = toaDo;
        } while (dangDuyet.x != diemDau.x || dangDuyet.y != diemDau.y);
        this.duongDi.addAll(duongDi);
    }

    public ArrayList<ToaDo> getDuongDi() {
        return duongDi;
    }

    ArrayList<ToaDo> duongDi;

    class Huongxet {
        public int x;
        public int y;
    }

    public class ToaDo {
        public int x;
        public int y;
        double fn, gn, hn;
        boolean daDuyet;
    }

}
