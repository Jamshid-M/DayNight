package uz.jamshid.daynight

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var defaultFlag = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        defaultFlag = window.decorView.systemUiVisibility
        applyDayNight(OnDayNightStateChanged.DAY)

        btnFirst.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, FragmentOne(), FragmentOne::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO){
            applyDayNight(OnDayNightStateChanged.DAY)
        }else{
            applyDayNight(OnDayNightStateChanged.NIGHT)
        }
    }

    private fun applyDayNight(state: Int){
        if (state == OnDayNightStateChanged.DAY){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.apply {
                    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            }else{
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryNight)
            }

            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorText))
            container.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }else{
            window.decorView.systemUiVisibility = defaultFlag
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryNight)
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorTextNight))
            container.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryNight))
        }

        supportFragmentManager.fragments.forEach {
            if(it is OnDayNightStateChanged){
                it.onDayNightApplied(state)
            }
        }
    }
}
