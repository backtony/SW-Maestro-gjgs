import React from "react";
import "./CouponManageItem.css";

interface CouponManageItemProps {
  lectureId: number;
  title: string;
  type: string;
  issueDate: string;
  closeDate: string;
  discountPrice: string;
  chargeCount: number;
  receivePeople: number;
  remainCount: number;
  checked: boolean;
  onClick: () => void;
}

export default class CouponManageItem extends React.Component<
  CouponManageItemProps,
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
            ></input>
          </div>
        </div>
        <div className="coupon-manage-header2 pretendard">
          <div>{this.props.title}</div>
        </div>
        <div className="coupon-manage-header3 pretendard">
          <div>{this.props.type}</div>
        </div>
        <div className="coupon-manage-header4 pretendard">
          <div>{this.props.issueDate.substr(0, 10)}</div>
        </div>
        <div className="coupon-manage-header4 pretendard">
          <div>{this.props.closeDate.substr(0, 10)}</div>
        </div>
        <div className="coupon-manage-header5 pretendard">
          <div>{this.props.discountPrice}</div>
        </div>
        <div className="coupon-manage-header6 pretendard">
          <div>{this.props.chargeCount}</div>
        </div>
        <div className="coupon-manage-header6 pretendard">
          <div>{this.props.receivePeople}</div>
        </div>
      </div>
    );
  }
}
