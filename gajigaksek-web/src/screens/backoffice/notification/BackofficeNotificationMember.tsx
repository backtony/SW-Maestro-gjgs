import React from "react";

interface BackofficeNotificationMemberProps {
  memberId: number;
  nickname: string;
  phone: string;
  startDate: string;
  role: string;
  checked: boolean;
  onClick: () => void;
}

export default class BackofficeNotificationMember extends React.Component<
  BackofficeNotificationMemberProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="coupon-manage-item">
        <div className="coupon-manage-header1 pretendard">
          <div>
            <input
              type="checkbox"
              checked={this.props.checked}
              onClick={() => this.props.onClick()}
              onChange={() => {
                let dd;
              }}
            ></input>
          </div>
        </div>
        <div className="coupon-manage-header3 pretendard">
          <div>{this.props.memberId}</div>
        </div>
        <div className="coupon-manage-header2 pretendard">
          <div>{this.props.nickname}</div>
        </div>
        <div className="coupon-manage-header4 pretendard">
          <div>{this.props.phone}</div>
        </div>
        <div className="coupon-manage-header4 pretendard">
          <div>{this.props.startDate}</div>
        </div>
        <div className="coupon-manage-header5 pretendard">
          <div>{this.props.role}</div>
        </div>
      </div>
    );
  }
}
