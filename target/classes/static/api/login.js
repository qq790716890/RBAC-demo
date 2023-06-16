function loginApi(data) {
    return $axios({
        'url': '/login',
        'method': 'post',
        data
    })
}

function logoutApi(data) {
    return $axios({
        'url': '/logout',
        'method': 'post',
        data
    })
}