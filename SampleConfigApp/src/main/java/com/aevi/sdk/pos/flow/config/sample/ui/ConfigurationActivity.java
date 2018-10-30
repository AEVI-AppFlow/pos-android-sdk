/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aevi.sdk.pos.flow.config.sample.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aevi.sdk.pos.flow.config.sample.DefaultConfigProvider;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;
import com.aevi.sdk.pos.flow.config.sample.R;
import com.aevi.sdk.pos.flow.config.sample.flowapps.AppEntityScanningHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigurationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SAVE_VIEW = "saveView";

    private int lastDisplayItem;
    private Menu refreshMenu;
    private boolean showRefreshMenu;

    @Inject
    AppEntityScanningHelper appEntityScanningHelper;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FpsConfig.getFpsConfigComponent().inject(this);

        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);
        setupNavigationBar();
        setInitialMenuItem(savedInstanceState);
    }

    private void setInitialMenuItem(Bundle savedInstanceState) {
        int defaultMenuItem = getDefaultMenuItem();
        if (savedInstanceState != null) {
            lastDisplayItem = savedInstanceState.getInt(SAVE_VIEW, defaultMenuItem);
        } else {
            lastDisplayItem = defaultMenuItem;
        }
    }

    protected int getDefaultMenuItem() {
        return navigationView.getMenu().getItem(0).getItemId();
    }

    private void setupNavigationBar() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_VIEW, lastDisplayItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFragment(lastDisplayItem);
    }

    private void showFragment(int menuId) {
        Fragment fragment = getFragmentForId(menuId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment, null);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commitAllowingStateLoss();
        }
    }

    private Fragment getFragmentForId(int menuId) {
        if (menuId == R.id.menu_flow_configuration) {
            setRefreshMenuVisible(true);
            return new FlowConfigurationFragment();
        } else {
            setRefreshMenuVisible(false);
            return new SettingsFragment();
        }
    }

    private void setRefreshMenuVisible(boolean visible) {
        showRefreshMenu = visible;
        if (refreshMenu != null) {
            refreshMenu.setGroupVisible(R.id.refresh_group, visible);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        this.refreshMenu = menu;
        setRefreshMenuVisible(showRefreshMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            case R.id.menu_refresh_apps:
                appEntityScanningHelper.reScanForPaymentAndFlowApps();
                return true;
            case R.id.menu_send_to_fps:
                DefaultConfigProvider.notifyConfigUpdated(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        lastDisplayItem = item.getItemId();
        new Handler().postDelayed(() -> showFragment(lastDisplayItem), 300);
        drawerLayout.closeDrawers();
        return true;
    }
}
