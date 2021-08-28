import './App.css';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {SignupPage} from "./authentication/pages/SignupPage";
import {LoginPage} from './authentication/pages/LoginPage';

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/signup">
          <SignupPage/>
        </Route>
        <Route path="/login">
          <LoginPage/>
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
