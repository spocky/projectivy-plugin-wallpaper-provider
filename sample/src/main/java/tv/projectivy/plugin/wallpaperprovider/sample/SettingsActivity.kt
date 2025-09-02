package tv.projectivy.plugin.wallpaperprovider.sample

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment

class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!packageManager.isApplicationInstalled("com.spocky.projengmenu")) {
            Toast.makeText(this, R.string.projectivy_not_installed, Toast.LENGTH_LONG).show()
        }

        val fragment: GuidedStepSupportFragment = SettingsFragment()
        if (savedInstanceState == null) {
            GuidedStepSupportFragment.addAsRoot(this, fragment, android.R.id.content)
        }
    }

    fun PackageManager.isApplicationInstalled(packageName: String): Boolean {
        return try {
            getApplicationInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }
}