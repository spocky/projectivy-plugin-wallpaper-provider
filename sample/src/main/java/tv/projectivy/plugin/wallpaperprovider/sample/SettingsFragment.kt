package tv.projectivy.plugin.wallpaperprovider.sample


import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist.Guidance
import androidx.leanback.widget.GuidedAction
import kotlin.CharSequence

class SettingsFragment : GuidedStepSupportFragment() {
    override fun onCreateGuidance(savedInstanceState: Bundle?): Guidance {
        return Guidance(
            getString(R.string.plugin_name),
            getString(R.string.plugin_description),
            getString(R.string.settings),
            AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_plugin)
        )
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        PreferencesManager.init(requireContext())

        val currentImageUrl = PreferencesManager.imageUrl
        val action = GuidedAction.Builder(context)
            .id(ACTION_ID_IMAGE_URL)
            .title(R.string.setting_image_url_title)
            .description(currentImageUrl)
            .editDescription(currentImageUrl)
            .descriptionEditable(true)
            .build()
        actions.add(action)
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
        when (action.id) {
            ACTION_ID_IMAGE_URL -> {
                val params: CharSequence? = action.editDescription
                findActionById(ACTION_ID_IMAGE_URL)?.description = params
                notifyActionChanged(findActionPositionById(ACTION_ID_IMAGE_URL))
                PreferencesManager.imageUrl = params.toString()
            }
        }
    }

    companion object {
        private const val ACTION_ID_IMAGE_URL = 1L
    }
}
