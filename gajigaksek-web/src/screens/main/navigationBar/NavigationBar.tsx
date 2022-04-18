import React from "react";
import NavigationBarButton from "./NavigationBarButton";
import "./NavigationBar.css";
import NavigationClassDropDown from "./NavigationClassDropDown";
import NavigationMoneyDropDown from "./NavigationMoneyDropDown";

interface NavigationBarProps {
  path: string;
}

interface NavigationBarStates {
  classDropdown: boolean;
  moneyDropdown: boolean;
}

export default class NavigationBar extends React.Component<
  NavigationBarProps,
  NavigationBarStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      classDropdown:
        window.location.href.indexOf("/myclass") > -1 ||
        window.location.href.indexOf("/class-manage") > -1 ||
        window.location.href.indexOf("/question-manage") > -1 ||
        window.location.href.indexOf("/review-manage") > -1 ||
        window.location.href.indexOf("/coupon-manage") > -1 ||
        window.location.href.indexOf("/chat") > -1,
      moneyDropdown: false,
    };
  }

  render() {
    return (
      <div className="navigation-bar">
        <div className="navigation-title-wrapper">
          <button
            className="jalnan"
            onClick={() => (window.location.href = "/main/home")}
          >
            가지각색
          </button>
        </div>
        <NavigationBarButton
          title={"홈"}
          onClick={() => {
            window.location.href = `/${this.props.path}/home`;
          }}
          on={
            window.location.href.split("/main")[1] === "/home" ||
            new RegExp("/add/class/*").test(
              window.location.href.split("/main")[1]
            )
          }
        />

        <NavigationClassDropDown
          dropdown={this.state.classDropdown}
          path={this.props.path}
          setDropDown={(dropdown: boolean) =>
            this.setState({ classDropdown: dropdown })
          }
        />
        <NavigationMoneyDropDown
          dropdown={this.state.moneyDropdown}
          setDropDown={(dropdown: boolean) =>
            this.setState({ moneyDropdown: dropdown })
          }
        />
      </div>
    );
  }
}
