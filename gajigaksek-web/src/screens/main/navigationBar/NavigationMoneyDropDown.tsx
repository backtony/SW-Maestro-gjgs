import React from "react";
import NavigationDropDownButton from "./NavigationDropDownButton";
import NavigationInnerButton from "./NavigationInnerButton";

interface NavigationMoneyDropDownProps {
  dropdown: boolean;
  setDropDown: (dropdown: boolean) => void;
}

export default class NavigationMoneyDropDown extends React.Component<
  NavigationMoneyDropDownProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="navi-dropdown">
        <NavigationDropDownButton
          title={"매출/정산"}
          onClick={() => this.props.setDropDown(!this.props.dropdown)}
          on={this.props.dropdown}
        />
        <div
          className={
            this.props.dropdown ? "navi-button-list-on" : "navi-button-list"
          }
        >
          <NavigationInnerButton
            title={"매출현황"}
            icon={"file-invoice-dollar"}
            onClick={() => {
              let dd;
            }}
            on={false}
          />
          <NavigationInnerButton
            title={"정산 신청"}
            icon={"pencil-alt"}
            onClick={() => {
              let dd;
            }}
            on={false}
          />
          <NavigationInnerButton
            title={"정산 내역"}
            icon={"list"}
            onClick={() => {
              let dd;
            }}
            on={false}
          />
        </div>
      </div>
    );
  }
}
