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
  }, [id, setResponse, setError]);
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
  }, [id, setResponse, setError]);
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