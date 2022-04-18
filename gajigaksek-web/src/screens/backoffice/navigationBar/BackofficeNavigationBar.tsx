import React from "react";
import NavigationBarButton from "../../main/navigationBar/NavigationBarButton";
import BackofficeNavigationBarButton from "./BackofficeNavigationBarButton";

interface NavigationBarProps {
  path: string;
}

interface NavigationBarStates {
  moneyDropdown: boolean;
}

export default class BackofficeNavigationBar extends React.Component<
  NavigationBarProps,
  NavigationBarStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      moneyDropdown: false,
    };
  }

  render() {
    return (
      <div className="navigation-bar">
        <div className="navigation-title-wrapper">
          <button
            className="jalnan"
            onClick={() => (window.location.href = "/backoffice/notification")}
          >
            가지각색
            <div className="jalnan">backoffice</div>
          </button>
        </div>
        <BackofficeNavigationBarButton
          title={"알림"}
          icon={"bell"}
          onClick={() => {
            window.location.href = `/${this.props.path}/notification`;
          }}
          on={window.location.href.split("/backoffice")[1] === "/notification"}
        />
        <BackofficeNavigationBarButton
          title={"클래스 검수"}
          icon={"money-check"}
          onClick={() => {
            window.location.href = `/${this.props.path}/class`;
          }}
          on={
            window.location.href.split("/backoffice")[1] === "/class" ||
            window.location.href.split("/backoffice")[1].split("/")[1] ===
              "class-detail"
          }
        />

        <BackofficeNavigationBarButton
          title={"공지사항"}
          icon={"clipboard-list"}
          onClick={() => {
            window.location.href = `/${this.props.path}/notice`;
          }}
          on={window.location.href.split("/backoffice")[1] === "/notice"}
        />
      </div>
    );
  }
}
