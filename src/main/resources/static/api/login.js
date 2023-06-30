function loginApi(data) {
    return $axios({
        'url': '/login',
        'method': 'post',
        data
    })
}

function getPublicKey() {
    return $axios({
        'url': '/getPublicKey',
        'method': 'get'
    })
}

function logoutApi(data) {
    return $axios({
        'url': '/logout',
        'method': 'post',
        data
    })
}