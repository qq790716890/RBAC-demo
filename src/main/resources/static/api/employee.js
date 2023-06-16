function getEmployeeList(data) {
    return $axios({
        'url': '/employee/list',
        'method': 'post',
        data
    })
}


function addEmployee(data) {
    return $axios({
        'url': '/employee/add',
        'method': 'post',
        data
    })
}



function updateEmployeeStatus(id,status) {
    return $axios({
        'url': '/employee/updateStatus/'+id,
        'method': 'put',
        'data': {
            status: status
        }
    })
}

function deleteEmployee(id) {
    return $axios({
        'url': '/employee/delete/'+id,
        'method': 'delete',
    })
}
function queryEmployeeById(id) {
    return $axios({
        'url': '/employee/query/'+id,
        'method': 'get',
    })
}

function queryLimitJobTitles(id) {
    return $axios({
        'url': '/jobTitle/queryLimitJobTitles/'+id,
        'method': 'get',
    })
}

function queryLimitDepartment(id) {
    return $axios({
        'url': '/department/queryLimitDepartment/'+id,
        'method': 'get',
    })
}