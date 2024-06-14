package id.kasrt.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import id.kasrt.R;

public class fragmentmain extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView profileImage;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentmain);

        // Initialize Views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        profileImage = toolbar.findViewById(R.id.profile_image);
        usernameText = toolbar.findViewById(R.id.username_text);

        // Simulate profile data
        profileImage.setImageResource(R.drawable.ic_profile_placeholder);

        // Initialize Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        if (email != null) {
            String username = email.substring(0, email.indexOf("@"));
            usernameText.setText(username);
        }

        // Initialize ViewPager2 and Adapter
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        // Disable swipe animation on ViewPager2
        viewPager2.setUserInputEnabled(false);

        // Configure TabLayout with ViewPager2
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            break;
                        case 1:
                            tab.setText("Bayar");
                            break;
                        case 2:
                            tab.setIcon(R.drawable.ic_scan); // Set icon for Scan tab
                            break;
                        case 3:
                            tab.setText("Kas");
                            break;
                        case 4:
                            tab.setText("Akun");
                            break;
                    }
                });
        tabLayoutMediator.attach();

        // Set listener for tab selection without animation
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
                updateToolbarVisibility(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Hide toolbar for tabs other than "Home" and "Akun"
        updateToolbarVisibility(viewPager2.getCurrentItem());
    }

    private void updateToolbarVisibility(int tabPosition) {
        if (tabPosition == 0 || tabPosition == 4) {
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        // Finish the current activity and all parent activities
        super.onBackPressed();
        finishAffinity();
    }
}
