import axios from 'axios';
import {useEffect, useState} from 'react';

export function useFindAllAvailabilitiesApi(page, pageSize, setResponse, setError, refetch) {
  useEffect(() => {
    const params = {page: page - 1, size: pageSize};
    axios
      .get('/api/availability', {params})
      .then(({data}) => setResponse(data))
      .catch(({response: {data}}) => setError != null && setError(data));
  }, [page, pageSize, setResponse, setError, refetch]);
}

export function useIncreaseAvailabilityApi(setResponse, setError) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (id != null) {
      axios
        .post(`/api/availability/${id}/increase`)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data))
        .finally(() => setId(null));
    }
  }, [id, setId, setResponse, setError]);
  return {setId};
}

export function useDecreaseAvailabilityApi(setResponse, setError) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (id != null) {
      axios
        .post(`/api/availability/${id}/decrease`)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data))
        .finally(() => setId(null));
    }
  }, [id, setId, setResponse, setError]);
  return {setId};
}

export function useCreateAvailabilityApi(request, setResponse, setError) {
  useEffect(() => {
    if (request != null) {
      axios
        .post(`/api/availability`, request)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data));
    }
  }, [request, setResponse, setError]);
}

export function useDeleteAvailabilityApi(setResponse, setError) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (id != null) {
      axios
        .delete(`/api/availability/${id}`)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data))
        .finally(() => setId(null));
    }
  }, [id, setId, setResponse, setError]);
  return {setId};
}

export function useUpdateAvailabilityApi(setResponse, setError) {
  const [request, setRequest] = useState(null);
  useEffect(() => {
    if (request != null) {
      axios
        .put(`/api/availability/${request.id}`, request.body)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError != null && setError(data))
        .finally(() => setRequest(null));
    }
  }, [request, setRequest, setResponse, setError]);
  return {setRequest};
}