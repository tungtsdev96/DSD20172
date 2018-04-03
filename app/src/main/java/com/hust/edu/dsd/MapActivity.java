package com.hust.edu.dsd;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hust.edu.dsd.adapter.CordinatorClick;
import com.hust.edu.dsd.adapter.ItemRcvMainAdapter;
import com.hust.edu.dsd.algorithm.SalesmanProblem;
import com.hust.edu.dsd.algorithm.SearchDirection;
import com.hust.edu.dsd.api.ApiFactory;
import com.hust.edu.dsd.api.BaseCallBack;
import com.hust.edu.dsd.fragment.DialogStateTree;
import com.hust.edu.dsd.model.Trees;
import com.hust.edu.dsd.model.WaterAndTree;
import com.hust.edu.dsd.model.WaterStation;
import com.hust.edu.dsd.utils.Constants;
import com.hust.edu.dsd.utils.Utils;

import java.util.ArrayList;
import java.util.logging.Handler;

import okhttp3.ResponseBody;

/**
 * Created by tungts on 3/13/2018.
 */

public class MapActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, CordinatorClick {

    int Matrix[][];
    RecyclerView rcv_map;
    ItemRcvMainAdapter adapter;
    NestedScrollView scroll_view;

    int currentX = 1;
    int currentY = 2;
    int oldType = 0;

    ImageView btn_up, btn_right, btn_left, btn_down;
    ImageView btn_work;

    ImageButton btn_menu;

    //water tree
    TextView tv_number_tree_registration, tv_number_tree_irrigated, tv_number_tree_irrigated_in_date;
    int number_tree_registration, number_tree_irrigated, number_tree_irrigated_in_date;
    ArrayList<Trees> list_trees;
    boolean isWatering;

    //water station
    EditText edt_input_water;
    Button btn_input_water, btn_registration;
    int waterHaved;
    WaterStation station;
    boolean isInputingWater;

    DrawerLayout drawer_map;
    NavigationView navi_map;

    ProgressDialog progressDialog;
    RelativeLayout content_view;

    @Override
    public int getLayoutResourceId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_map;
    }

    @Override
    protected void addControls() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu");
        progressDialog.show();
        content_view = findViewById(R.id.content_view); content_view.setVisibility(View.INVISIBLE);
        scroll_view = findViewById(R.id.scroll_view);
        drawer_map = findViewById(R.id.drawer_map);
        navi_map = findViewById(R.id.navi_map);
        navi_map.setNavigationItemSelectedListener(this);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);
        innitMennu();
        innitRcv();
        innitButtonControll();
    }

    private void innitMennu() {
        tv_number_tree_registration = findViewById(R.id.tv_number_tree_registration);
        tv_number_tree_irrigated = findViewById(R.id.tv_number_tree_irrigated);
        tv_number_tree_irrigated_in_date = findViewById(R.id.tv_number_tree_irrigated_in_date);

        //input water
        edt_input_water = findViewById(R.id.edt_input_water);
        btn_registration = findViewById(R.id.btn_registration);
        btn_input_water = findViewById(R.id.btn_input_water);
        btn_registration.setOnClickListener(this);
        btn_input_water.setOnClickListener(this);

    }

    //joy stick
    private void innitButtonControll() {
        btn_up = findViewById(R.id.btn_up);
        btn_up.setOnClickListener(this);
        btn_right = findViewById(R.id.btn_right);
        btn_right.setOnClickListener(this);
        btn_left = findViewById(R.id.btn_left);
        btn_left.setOnClickListener(this);
        btn_down = findViewById(R.id.btn_down);
        btn_down.setOnClickListener(this);
        btn_work = findViewById(R.id.btn_work);
        btn_work.setOnClickListener(this);
    }

    //map
    private void innitRcv() {
        rcv_map = findViewById(R.id.rcv_map);
        innitMatrix();
    }

    private void innitAdapter() {
        Log.e("current",currentX + " " +currentY);
        Matrix[currentX][currentY] = Constants.START;
        adapter = new ItemRcvMainAdapter(this, Matrix);
        adapter.setOnCordinatorClick(this);
        rcv_map.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        rcv_map.setLayoutManager(linearLayoutManager);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                content_view.setVisibility(View.VISIBLE);
            }
        },2000);
        if (isRegis){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerWaterTree();
                    isRegis = false;
                }
            },1000);
        }
    }

    //innnit toa do ban do
    private void innitMatrix() {
        Matrix = new int[50][50];
        //get all tree, get all water station
        ApiFactory.getApiHust().getAllTrees().enqueue(new BaseCallBack<ArrayList<Trees>>(this) {
            @Override
            public void onSuccess(ArrayList<Trees> result) {
                for (Trees trees : result) {
                    if (trees.getCurrent_water() == 0) {
                        Matrix[trees.getX()][trees.getY()] = Constants.TREE_IRRIGATED;
                        continue;
                    }
                    Matrix[trees.getX()][trees.getY()] = Constants.TREES;
                }
                ApiFactory.getApiHust().getAllWaterStation().enqueue(new BaseCallBack<ArrayList<WaterStation>>(MapActivity.this) {
                    @Override
                    public void onSuccess(ArrayList<WaterStation> result) {
                        for (WaterStation station : result) {
                            Matrix[station.getX()][station.getY()] = Constants.WATER_SOURCE;
                        }
                        innitAdapter();
                    }
                });
            }
        });

    }

    private void scrollToY(final int y) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scroll_view.scrollTo(0, (int) Utils.convertDpToPixel(y * 15, MapActivity.this));
            }
        });
    }

    private void scrollToX(final int x) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rcv_map.getLayoutManager().scrollToPosition((x - 2) > 0 ? x - 2 : x);
            }
        });
    }

    boolean isRegis;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                moveUp();
                break;
            case R.id.btn_right:
                moveRight();
                break;
            case R.id.btn_left:
                moveLeft();
                break;
            case R.id.btn_down:
                moveDown();
                break;
            case R.id.btn_work:
                waterTheTrees();
                break;
            case R.id.btn_menu:
                if (!drawer_map.isDrawerVisible(GravityCompat.START)) {
                    drawer_map.openDrawer(GravityCompat.START);
                } else {
                    drawer_map.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.btn_registration:
                isRegis = true;
                progressDialog.show();
                innitMatrix();
                break;
        }
    }

    //thuc hien tuoi cay
    private void waterTheTrees() {
        //check dung dung vtri chua
        if (oldType != Constants.TREE_REGISTRATION) {
            Toast.makeText(this, "Ở đây không có cây để tưới", Toast.LENGTH_SHORT).show();
            return;
        }

        //update trang thai cho cay, gửi lên server va tu tien mot buoc.
        //sent data to server
        final Trees trees = getTreesByXY();
        ApiFactory.getApiHust().waterTree(trees.getTree_id(), AccountUtil.getInstance(this).getStaff().getStaff_id(), trees.getCurrent_water()).enqueue(new BaseCallBack<ResponseBody>(this) {
            @Override
            public void onSuccess(ResponseBody result) {
                updateStateForTreeWater(trees);
                Toast.makeText(MapActivity.this, "Da tuoi cay " + trees.getX() + "," + trees.getY(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateStateForTreeWater(Trees trees) {
        currentX = trees.getX();
        currentY = trees.getY();

        waterHaved -= trees.getCurrent_water();
        number_tree_irrigated++;
        number_tree_irrigated_in_date++;

        updateNumberTreeWater();
        adapter.notify(currentX, currentY, Constants.TREE_IRRIGATED);
        oldType = Constants.TREE_IRRIGATED;

        if (number_tree_irrigated == number_tree_registration) {
            isWatering = false;
            updateNumberTreeWater();
        } else {
            searchDirectionToTree(list_trees.get(xopt[number_tree_irrigated + 1] - 1));
        }

    }

    private Trees getTreesByXY() {
        for (Trees trees : list_trees) {
            if (trees.getX() == currentX && trees.getY() == currentY)
                return trees;
        }
        return null;
    }

    ///move
    private void moveDown() {
        adapter.notify(currentX, currentY, oldType);
        if (currentY < 50) {
            currentY++;
        }
        oldType = Matrix[currentX][currentY];
        adapter.notify(currentX, currentY, Constants.START);
    }

    private void moveLeft() {
        adapter.notify(currentX, currentY, oldType);
        if (currentX > 0) {
            currentX--;
        }
        oldType = Matrix[currentX][currentY];
        adapter.notify(currentX, currentY, Constants.START);
    }

    private void moveRight() {
        adapter.notify(currentX, currentY, oldType);
        if (currentX < 50) {
            currentX++;
        }
        oldType = Matrix[currentX][currentY];
        adapter.notify(currentX, currentY, Constants.START);
    }

    private void moveUp() {
        adapter.notify(currentX, currentY, oldType);
        if (currentY > 0) {
            currentY--;
        }
        oldType = Matrix[currentX][currentY];
        adapter.notify(currentX, currentY, Constants.START);
    }

    int countGoIncorrent = 0;

    private boolean checkCorrectDirection(int x, int y) {
        if (Matrix[x][y] != Constants.DIRECTION) {
            if (countGoIncorrent == 3) {
                //logout
                //gui thong bao cho server
            }
            countGoIncorrent++;
            return false;
        }
        return true;
    }

    /////drawer layout
    public void closeDrawer() {
        if (drawer_map.isDrawerVisible(GravityCompat.START)) {
            drawer_map.closeDrawer(GravityCompat.START);
        }
    }

    ////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer_map.isDrawerOpen(GravityCompat.START)) {
            drawer_map.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //check dang tuoi cay khong
            if (isWatering) {
                Toast.makeText(this, "Tuoi xong da roi thoat", Toast.LENGTH_SHORT).show();
                return true;
            }
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 222) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        call = false;
                        return;
                    }
                }
                call = true;
            }
        }
    }

    boolean call = true;

    private void call() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 222);
        } else {
            Uri uri = Uri.parse("tel:" + "096968888");
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    ///navi
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        switch (item.getItemId()) {
            case R.id.menu_current_location:
                scrollToX(currentX);
                scrollToY(currentY);
                break;
            case R.id.menu_goto_watersource:
                if (isWatering) {
                    showAlert("Thông báo", "Hãy tưới cây xong rồi ra lấy nước");
                } else {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ApiFactory.getApiHust().getNearest(currentX, currentY).enqueue(new BaseCallBack<WaterStation>(MapActivity.this) {
                                @Override
                                public void onSuccess(final WaterStation result) {
                                    final SearchDirection searchDirection = new SearchDirection(currentX, currentY, result.getX(), result.getY(), Matrix);
                                    searchDirection.searchDirection();
                                    if (!lastDirection.isEmpty()){
                                        ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
                                            @Override
                                            public void onDone() {
                                                lastDirection.clear();
                                                lastDirection.addAll(searchDirection.getDuongDi());
                                                ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
                                                    @Override
                                                    public void onDone() {
                                                        Toast.makeText(MapActivity.this, "Đi lấy nước ở " + result.getWater_location(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).showDirection(lastDirection,Constants.DIRECTION,MapActivity.this);
                                            }
                                        }).showDirection(lastDirection,Constants.BACKGROUND,MapActivity.this);
                                    } else {
                                        lastDirection.addAll(searchDirection.getDuongDi());
                                        ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
                                            @Override
                                            public void onDone() {
                                                Toast.makeText(MapActivity.this, "Đi lấy nước ở " + result.getWater_location(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).showDirection(lastDirection,Constants.DIRECTION,MapActivity.this);
                                    }
                                }
                            });
                        }
                    }, 500);
                }
                break;
            case R.id.menu_goto_water_tree:
                Toast.makeText(this, "hien duong tuoi cay", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_sos:
                showDialogSos();
                break;
            case R.id.menu_call:
                call();
                break;
        }
        return true;
    }

    ///dialog sos
    private void showDialogSos() {
        DialogStateTree.newInstance().show(getFragmentManager(), "SOS");
    }

    @Override
    public void cordinatorClick(int x, int y, int type) {
        switch (type) {
            case Constants.TREES:
                Toast.makeText(this, "Toa do cay" + x + " " + y, Toast.LENGTH_SHORT).show();
                break;
            case Constants.TREE_REGISTRATION:
                notifyAdapter(currentX,currentY,oldType);
                oldType = Constants.TREE_REGISTRATION;
                currentY = y;
                currentX = x;
                waterTheTrees();
                break;
            case Constants.START:
                Toast.makeText(this, "Dang o" + x + " " + y, Toast.LENGTH_SHORT).show();
                break;
            case Constants.TREE_IRRIGATED:
                Toast.makeText(this, "Cay da dc tuoi", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    ////gui dang ki tuoi server tra ve cac cay va tram nuoc
    private void registerWaterTree() {
        if (edt_input_water.getText().toString().trim().length() == 0) {
            showAlert("Thông báo", "Nhập lượng nước đang có đi đã");
            return;
        }

        waterHaved = Integer.parseInt(edt_input_water.getText().toString().trim());
        if (waterHaved <= 0) {
            showAlert("Thông báo", "Nhập sai rồi");
            return;
        }

        //start get tree, search direction
//        progressDialog.show();
        ApiFactory.getApiHust().getWaterAndTree(currentX, currentY, waterHaved).enqueue(new BaseCallBack<WaterAndTree>(this) {
            @Override
            public void onSuccess(WaterAndTree result) {
                station = result.getWaterStation();
                adapter.notify(station.getX(), station.getY(), Constants.WATER_SOURCE);
                list_trees = result.getTrees();
                number_tree_registration = list_trees.size();
                searchDirectionToTree(list_trees);
                isWatering = true;
                updateUI(list_trees);
            }
        });
    }

    //search duuong da vao diem dau va danh sach cay
    int xopt[];
    private void searchDirectionToTree(final ArrayList<Trees> list_trees) {
        final double cost[][] = new double[list_trees.size() + 1][list_trees.size() + 1];
        cost[0][0] = 0;
        for (int i = 0; i < list_trees.size(); i++) {
            //current x, current y
            cost[0][i + 1] = Utils.distance(currentX, currentY, list_trees.get(i).getX(), list_trees.get(i).getY());
            cost[i + 1][0] = cost[0][i + 1];
        }

        for (int i = 0; i < list_trees.size() - 1; i++) {
            for (int j = i; j < list_trees.size(); j++) {
                if (i == j) {
                    cost[i + 1][j + 1] = 0;
                    continue;
                }
                cost[i + 1][j + 1] = Utils.distance(list_trees.get(i).getX(), list_trees.get(i).getY(), list_trees.get(j).getX(), list_trees.get(j).getY());
                cost[j + 1][i + 1] = cost[i + 1][j + 1];
            }
        }

        SalesmanProblem problem = new SalesmanProblem(list_trees.size() + 1, cost);
        problem.branchAndBound(1);
        this.xopt = problem.getXopt();
        searchDirectionToTree(list_trees.get(xopt[number_tree_irrigated + 1] - 1));
    }

    //dua ra duwong phu hop
    ArrayList<SearchDirection.ToaDo> lastDirection = new ArrayList<>();
    private void searchDirectionToTree(final Trees tree) {
        if (!lastDirection.isEmpty()){
            ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
                @Override
                public void onDone() {
                    lastDirection.clear();
                    showDirectionToTree(tree);
                }
            }).showDirection(lastDirection,Constants.BACKGROUND,MapActivity.this);
            return;
        }
        showDirectionToTree(tree);
//        showListToaDo(searchDirection.getDuongDi(),Constants.DIRECTION);
//        for (int j = 1; j < xopt.length - 1; j++) {
//            Trees tree1 = list_trees.get(xopt[j] - 1);
//            Trees tree2 = list_trees.get(xopt[j + 1] - 1);
//            SearchDirection search = new SearchDirection(tree1.getX(), tree1.getY(), tree2.getX(), tree2.getY(), Matrix);
//            search.searchDirectionToTree();
//            showListToaDo(search.getDuongDi());
//        }
    }

    private void showDirectionToTree(final Trees tree) {
        //current -> tree1
        SearchDirection searchDirection = new SearchDirection(currentX, currentY, tree.getX(), tree.getY(), Matrix);
        searchDirection.searchDirection();
        lastDirection.addAll(searchDirection.getDuongDi());
        ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
            @Override
            public void onDone() {
                Toast.makeText(MapActivity.this, "Hãy đến tưới cây " + tree.getTree_name(), Toast.LENGTH_SHORT).show();
            }
        }).showDirection(lastDirection,Constants.DIRECTION,MapActivity.this);
    }

    public void notifyAdapter(int x, int y, int type){
        adapter.notify(x, y, type);
    }

    int dem;
    android.os.Handler handler = new android.os.Handler();
    private void updateUI(final ArrayList<Trees> list_trees) {
        dem = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                adapter.notify(list_trees.get(dem).getX(), list_trees.get(dem).getY(), Constants.TREE_REGISTRATION);
                dem++;
                handler.postDelayed(this,200);
                if (dem == list_trees.size()) handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(runnable,10);
        btn_registration.setVisibility(View.GONE);
        edt_input_water.setVisibility(View.GONE);
        updateNumberTreeWater();
        isWatering = true;
    }

    private void updateNumberTreeWater() {
        if (isWatering) {
            tv_number_tree_registration.setVisibility(View.VISIBLE);
            tv_number_tree_irrigated.setVisibility(View.VISIBLE);
            tv_number_tree_irrigated_in_date.setVisibility(View.VISIBLE);
            tv_number_tree_registration.setText("Số cây đã đăng kí: " + number_tree_registration);
            tv_number_tree_irrigated.setText("Số cây đã tưới: " + number_tree_irrigated + "/" + number_tree_registration);
            tv_number_tree_irrigated_in_date.setText("Số cây đã tưới trong buổi: " + number_tree_irrigated_in_date);
            return;
        }

        if (number_tree_irrigated_in_date >= 20) {
            showAlert("Thông Báo", "Bạn đã hoàn thành xong công việc hôm nay");
            return;
        }

        ShowDirection.getInstance(new ShowDirection.ShowDirectionDone() {
            @Override
            public void onDone() {
                lastDirection.clear();
            }
        }).showDirection(lastDirection, Constants.BACKGROUND, MapActivity.this);
        tv_number_tree_registration.setVisibility(View.GONE);
        number_tree_irrigated = 0;
        tv_number_tree_irrigated.setVisibility(View.GONE);
        number_tree_registration = 0;
        tv_number_tree_irrigated_in_date.setVisibility(View.GONE);
        btn_registration.setVisibility(View.VISIBLE);
        edt_input_water.setVisibility(View.VISIBLE);

    }

}
