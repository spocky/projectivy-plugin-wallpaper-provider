package tv.projectivy.plugin.wallpaperprovider.sample

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment

class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment: GuidedStepSupportFragment = SettingsFragment()
        if (savedInstanceState == null) {
            GuidedStepSupportFragment.addAsRoot(this, fragment, android.R.id.content)
        }
    }
}