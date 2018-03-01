package sk.styk.martin.apkanalyzer.ui.activity.repackageddetection

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repackaged_detection.*
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.PieChartData
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.upload.task.RepackagedDetectionLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class RepackagedDetectionFragment : Fragment(), RepackagedDetectionContract.View {

    private lateinit var presenter: RepackagedDetectionContract.Presenter

    init {
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RepackagedDetectionPresenter(RepackagedDetectionLoader(currentData(), context), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_repackaged_detection, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize(currentData())
    }

    override fun showLoading() {
        repackaged_loading_data.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        repackaged_loading_data.visibility = View.GONE
    }

    override fun showAppOk(result: RepackagedDetectionResult,
                           appSignaturePieChartData: PieChartData,
                           majoritySignaturePieChartData: PieChartData,
                           signatureColumnChartData: ColumnChartData) {
        repackaged_image.setImageResource(R.drawable.ic_ok)
        repackaged_header.text = getString(R.string.repackaged_result_ok)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalDifferentRepackagedApps,
                result.totalDifferentRepackagedApps,
                result.totalRepackagedApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_ok_description)
        showRepackagedDetectionDetails(result, appSignaturePieChartData, majoritySignaturePieChartData, signatureColumnChartData)
    }

    override fun showAppNotOk(result: RepackagedDetectionResult,
                              appSignaturePieChartData: PieChartData,
                              majoritySignaturePieChartData: PieChartData,
                              signatureColumnChartData: ColumnChartData) {
        repackaged_image.setImageResource(R.drawable.ic_warning)
        repackaged_header.text = getString(R.string.repackaged_result_nok)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalDifferentRepackagedApps,
                result.totalDifferentRepackagedApps,
                result.totalRepackagedApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_nok_description)
        showRepackagedDetectionDetails(result, appSignaturePieChartData, majoritySignaturePieChartData,signatureColumnChartData)
    }

    override fun showAppNotDetected(result: RepackagedDetectionResult,
                                    appSignaturePieChartData: PieChartData,
                                    majoritySignaturePieChartData: PieChartData,
                                    signatureColumnChartData: ColumnChartData) {
        repackaged_image.setImageResource(R.drawable.ic_android)
        repackaged_header.text = getString(R.string.repackaged_result_insufficient)
        repackaged_description.text = resources.getQuantityString(R.plurals.repackaged_result_detection_description_general,
                result.totalDifferentRepackagedApps,
                result.totalDifferentRepackagedApps,
                result.totalRepackagedApps)
        repackaged_description_detail.text = getString(R.string.repackaged_result_insufficient_description)
        showRepackagedDetectionDetails(result, appSignaturePieChartData, majoritySignaturePieChartData,signatureColumnChartData)
    }

    override fun showNoInternetConnection() {
        repackaged_image.setImageResource(R.drawable.ic_cloud_upload)
        repackaged_header.text = getString(R.string.no_internet_connection)
        repackaged_description.text = getString(R.string.no_internet_connection_description)
    }

    override fun showUploadNotAllowed() {
        repackaged_image.setImageResource(R.drawable.ic_allow_upload)
        repackaged_header.text = getString(R.string.metadata_upload_not_allowed)
        repackaged_description.text = getString(R.string.metadata_upload_not_allowed_description)
    }

    override fun showDetectionError() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.repackaged_error)
        repackaged_description.text = getString(R.string.repackaged_error_description)
    }

    override fun showServiceUnavailable() {
        repackaged_image.setImageResource(R.drawable.ic_not_available)
        repackaged_header.text = getString(R.string.service_not_available)
        repackaged_description.text = getString(R.string.service_not_available_description)
    }

    private fun currentData(): AppDetailData {
        return arguments.getParcelable(DATA)
    }

    private fun showRepackagedDetectionDetails(result: RepackagedDetectionResult,
                                               appSignaturePieChartData: PieChartData,
                                               majoritySignaturePieChartData: PieChartData,
                                               signatureColumnChartData: ColumnChartData) {
        repackaged_card_signatures_chart.visibility = View.VISIBLE
        repackaged_global_signature_chart.isZoomEnabled = false
        repackaged_global_signature_chart.columnChartData = signatureColumnChartData
        repackaged_global_signature_chart.startDataAnimation()

        repackaged_card_app_signature.visibility = View.VISIBLE
        repackaged_app_signature_chart.pieChartData = appSignaturePieChartData
        repackaged_app_signature_chart.startDataAnimation()
        repackaged_app_signature_description.text = getString(R.string.repackaged_result_detection_app_signature, result.percentageSameSignature.toString())

        repackaged_card_majority_signature.visibility = View.VISIBLE
        repackaged_majority_signature_chart.pieChartData = majoritySignaturePieChartData
        repackaged_majority_signature_chart.startDataAnimation()
        repackaged_majority_signature_description.text = getString(R.string.repackaged_result_detection_majority_signature, result.percentageMajoritySignature.toString())
    }

    companion object {
        const val TAG = "RepackagedDetectionFragment"

        private const val DATA = "data"

        fun newInstance(data: AppDetailData): RepackagedDetectionFragment {
            val frag = RepackagedDetectionFragment()
            val args = Bundle()
            args.putParcelable(DATA, data)
            frag.arguments = args
            return frag
        }
    }
}