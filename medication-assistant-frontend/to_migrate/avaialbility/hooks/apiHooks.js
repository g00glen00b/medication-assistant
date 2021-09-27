import axios from 'axios';
import {useEffect, useState} from 'react';

export function useFindAllAvailabilitiesApi(page, pageSize, setResponse, setError = () => {}) {
  useEffect(() => {
    const params = {page: page - 1, pageSize};
    axios
      .get('/api/availability', {params})
      .then(({data}) => setResponse(data))
      .catch(({response: {data}}) => setError(data));
  }, [page, pageSize, setResponse, setError]);
}

export function useIncreaseAvailabilityApi(setResponse, setError = () => {}) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (id != null) {
      axios
        .post(`/api/availability/${id}/increase`)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError(data))
        .finally(() => setId(null));
    }
  }, [id, setResponse, setError, setId]);
  return {setId};
}

export function useDecreaseAvailabilityApi(setResponse, setError = () => {}) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (id != null) {
      axios
        .post(`/api/availability/${id}/decrease`)
        .then(({data}) => setResponse(data))
        .catch(({response: {data}}) => setError(data))
        .finally(() => setId(null));
    }
  }, [id, setResponse, setError, setId]);
  return {setId};
}