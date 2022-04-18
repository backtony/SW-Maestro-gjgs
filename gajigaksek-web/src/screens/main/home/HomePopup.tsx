import React from "react";
import LoginController from "../../../services/controllers/LoginController";
import "./HomePopup.css";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface HomePopupProps {
  on: boolean;
  setVisible: (visible: boolean) => void;
}

interface HomePopupStates {
  nickname: string;
  text: string;
  profile: any;
}

export default class HomePopup extends React.Component<
  HomePopupProps,
  HomePopupStates
> {
  private inputRef;
  private imgRef;
  private textareaRef;

  constructor(props: any) {
    super(props);
    this.inputRef = React.createRef<HTMLInputElement>();
    this.imgRef = React.createRef<HTMLImageElement>();
    this.textareaRef = React.createRef<HTMLTextAreaElement>();
    this.state = { text: "", profile: undefined, nickname: "" };
    this.getProfile(cookies.get("memberId"));
  }

  private async getProfile(memberId: number) {
    if (!memberId) return;
    try {
      const res = await LoginController.getProfile(memberId);
      this.setState({
        profile: res?.data,
        nickname: res?.data.nickname,
        text: res?.data.profileText,
      });
    } catch (e) {}
  }

  private handleCameraClick() {
    (this.inputRef.current as HTMLInputElement).addEventListener(
      "change",
      (e: any) => this.handleFileUpload(e)
    );
    (this.inputRef.current as HTMLInputElement).click();
  }

  private handleFileUpload(e: any) {
    const files = e.target.files;
    if (!files || files.length <= 0) return;
    const selectedFile = files[0];
    const reader = new FileReader();

    const imgtag = this.imgRef.current as HTMLImageElement;
    imgtag.title = selectedFile.name;

    reader.onload = function (event) {
      if (event && event.target) imgtag.src = event.target.result as string;
    };

    reader.readAsDataURL(selectedFile);

    const formData = new FormData();
    formData.append("file", selectedFile);

    this.postProfileImg(formData);
  }

  private async postProfileImg(formData: FormData) {
    try {
      await LoginController.postProfileImg(formData);
    } catch (e) {}
  }

  private async putNickname(nickname: string) {
    const nicknameRegex = /^[ㄱ-ㅎ가-힣a-z0-9_-]{2,20}$/;
    if (!nickname || !nicknameRegex.test(nickname)) {
      alert("닉네임을 다시 입력해주세요. (특수문자X, 2자이상 20자이하)");
      return false;
    }
    try {
      await LoginController.putNickname(nickname);
      alert("닉네임 변경이 완료되었습니다.");
    } catch (e) {}
  }

  private async putProfileText(text: string) {
    try {
      await LoginController.putProfileText({
        profileText: text,
      });
      this.props.setVisible(false);
    } catch (e) {}
  }

  render() {
    return (
      <div
        className={`home-popup-container ${
          this.props.on ? "home-popup-container-on" : ""
        }`}
      >
        <div className="home-popup">
          <div className="home-popup-title-container">
            <div className="home-popup-title pretendard">
              디렉터 프로필 편집하기
            </div>
            <button
              className="home-popup-exit"
              onClick={() => this.props.setVisible(!this.props.on)}
            >
              <i className="fas fa-times fa-2x"></i>
            </button>
          </div>
          <div className="home-popup-main-wrapper">
            <div className="home-popup-main-container">
              <div className="home-popup-img-container">
                <img
                  ref={this.imgRef}
                  src={
                    this.state.profile ? this.state.profile.imageFileUrl : ""
                  }
                />
                <button
                  className="home-popup-img-button"
                  onClick={() => this.handleCameraClick()}
                >
                  <i className="fas fa-camera fa-lg"></i>
                </button>
                <input
                  type="file"
                  className="home-popup-img-input"
                  accept="image/png, image/jpeg"
                  ref={this.inputRef}
                ></input>
              </div>
              <div className="home-popup-content-container">
                <div className="home-popup-nickname-container">
                  <div className="home-popup-nickname-title pretendard">
                    닉네임
                  </div>
                  <div className="home-popup-nickname-input-wrapper">
                    <input
                      className="home-popup-nickname-input pretendard"
                      value={this.state.nickname}
                      onChange={(e: any) =>
                        this.setState({ nickname: e.target.value })
                      }
                    ></input>
                    <button
                      className="home-popup-edit-button pretendard"
                      onClick={() => this.putNickname(this.state.nickname)}
                    >
                      변경하기
                    </button>
                  </div>
                </div>
                <div className="home-popup-intro-container">
                  <div className="home-popup-intro-title pretendard">소개</div>
                  <div className="home-popup-intro-textarea-wrapper">
                    <textarea
                      ref={this.textareaRef}
                      className="home-popup-intro-textarea pretendard"
                      value={this.state.text}
                      onChange={(e: any) =>
                        this.setState({ text: e.target.value })
                      }
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
            <button
              className="home-popup-button pretendard"
              onClick={() => this.putProfileText(this.state.text)}
            >
              저장
            </button>
          </div>
        </div>
      </div>
    );
  }
}
