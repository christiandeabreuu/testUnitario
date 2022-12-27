package br.com.zup.ezuppers.viewstate

sealed class ViewState<out T> {
    class Success<T>(val data: T) : ViewState<T>()
    class Error(val throwable: Throwable) : ViewState<Nothing>()
    class Loading(val loading: Boolean) : ViewState<Nothing>() // mudei aqui pra colocar como estado inicial

}