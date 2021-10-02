import './App.css';
import {BrowserRouter as Router, Redirect, Route, Switch} from 'react-router-dom';
import {LoginPage} from './authentication/pages/LoginPage';
import {AuthenticationContextProvider} from './authentication/components/AuthenticationContext';
import {SignupPage} from './authentication/pages/SignupPage';
import {ApplicationRouteWrapper} from './shared/components/ApplicationRouteWrapper';

function App() {
  return (
    <AuthenticationContextProvider>
      <Router>
        <Switch>
          <Route exact path="/signup">
            <SignupPage/>
          </Route>
          <Route exact path="/login">
            <LoginPage/>
          </Route>
          <Route path="/app">
            <ApplicationRouteWrapper/>
          </Route>
          <Route exact path="/">
            <Redirect to="/app/availability"/>
          </Route>
        </Switch>
      </Router>
    </AuthenticationContextProvider>
  );
}

export default App;
