package utils.constant

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import utils.ProgressDialogFragment


fun AppCompatActivity.replaceFragment(fragment: Fragment, placeHolder: Int, tag: String){
    val fragmentTransaction = supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(placeHolder, fragment, tag)
    fragmentTransaction.commit()
}

fun Fragment.replaceFragment(fragment: Fragment,placeHolder: Int, tag: String){
    val fragmentTransaction = childFragmentManager.beginTransaction()
    fragmentTransaction.replace(placeHolder, fragment, tag)
    fragmentTransaction.commit()
}

fun FragmentManager.showProgressDialog(title: String? = null, message: String) {
    hideProgressDialog()
    val progressDialogFragment = ProgressDialogFragment.newInstance(title, message)
    progressDialogFragment.show(this.beginTransaction(), ProgressDialogFragment.TAG)
}

fun FragmentManager.hideProgressDialog() {
    val prev = this.findFragmentByTag(ProgressDialogFragment.TAG)
    if (prev is DialogFragment) {
        prev.dismiss()
    }
}