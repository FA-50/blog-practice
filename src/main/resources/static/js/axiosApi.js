const apiClient = axios.create({
    timeout: 5000,                     // 요청 제한 시간 (5초)
    headers: {                         // 기본 Header 설정
        "Content-Type": "application/json"
    }
});
export const commentPost = (path,createRequest)=>apiClient.post(path,createRequest);

export const commentPostWithParams = (path,createRequest,postId)=>apiClient.post(path,createRequest,{ params : {
    postId : postId
    } })

export const commentDelete = (path) => apiClient.delete(path);

export const commentUpdate = (path,createRequest) => apiClient.put(path,createRequest)

export const postDelete = (path) => apiClient.delete(path)

export const categoryUpdate = (path,updateRequest) => apiClient.put(path,updateRequest)

export const categoryDelete = (path) => apiClient.delete(path)
