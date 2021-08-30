async function fetchJsonConfig(url) {
  const credentials = 'same-origin';
  const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };
  const response = await fetch(url, {headers, credentials});
  return await response.json();
}

window.onload = async function() {
  const uiConfig = await fetchJsonConfig('/swagger-resources/configuration/ui');
  const securityConfig = await fetchJsonConfig('/swagger-resources/configuration/security');
  const resources = await fetchJsonConfig('/swagger-resources');
  const cookies = new UniversalCookie();
  window.ui = SwaggerUIBundle({
    url: '',
    dom_id: '#swagger-ui',
    urls: resources,
    ...securityConfig,
    ...uiConfig,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    oauth2RedirectUrl: '/webjars/swagger-ui/3.51.2/oauth2-redirect.html',
    showMutatedRequest: true,
    modelPropertyMacro: null,
    parameterMacro: null,
    requestInterceptor: request => {
      request.headers['X-XSRF-TOKEN'] = cookies.get('XSRF-TOKEN');
      return request;
    },
    layout: 'StandaloneLayout'
  });
};