package ps.sipnas.di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.data.rest.SipnasRepositoryImpl
import ps.sipnas.ui.home.detail.DetailSPDViewModel
import ps.sipnas.ui.home.done.DoneViewModel
import ps.sipnas.ui.home.process.ProcessViewModel
import ps.sipnas.ui.home.statistic.StatisticViewModel
import ps.sipnas.ui.home.upload.UploadViewModel
import ps.sipnas.ui.home.upload.kegiatan.KegiatanViewModel
import ps.sipnas.ui.home.upload.kegiatan.detail.DetailKegiatanViewModel
import ps.sipnas.ui.home.upload.spj.SpjViewModel
import ps.sipnas.ui.home.upload.spj.detail.DetailSpjViewModel
import ps.sipnas.ui.login.LoginViewModel
import ps.sipnas.ui.notification.NotificationViewModel
import ps.sipnas.ui.profile.ProfileViewModel
import ps.sipnas.ui.profile.edit.EditProfileViewModel
import ps.sipnas.utils.RxEditTextBinding

/**
 **********************************************
 * Created by ukie on 10/4/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right `
 */

/**
 *  ViewModel inject
 */
val sipnasViewModel = module {
    // TODO define view model to inject
    viewModel { LoginViewModel(get()) }
    viewModel { ProcessViewModel(get()) }
    viewModel { SpjViewModel(get()) }
    viewModel { KegiatanViewModel(get()) }
    viewModel { DoneViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UploadViewModel(get()) }
    viewModel { DetailSPDViewModel(get()) }
    viewModel { DetailSpjViewModel(get()) }
    viewModel { DetailKegiatanViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
}

val globalModule = module {
    single { RxEditTextBinding() }
}

val dataModule = module(createOnStart = true) {
    //TODO inject Repository when implement on RepositoryImpl
    single<SipnasRepository> { SipnasRepositoryImpl(get()) }
}


val sipnasApp = listOf(sipnasViewModel, dataModule, networkModule, globalModule)