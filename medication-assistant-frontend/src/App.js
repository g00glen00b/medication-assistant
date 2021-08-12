import './App.css';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {SignupPage} from "./authentication/pages/SignupPage";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/signup">
          <SignupPage/>
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
