import ShareModal from '@/screens/modal/ShareModal';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureDetailViewHeaderProps {
  navigation: any;
  lecture: any;
}

interface LectureDetailViewHeaderStates {
  shareModalVisible: boolean;
}

export default class LectureDetailViewHeader extends React.Component<
  LectureDetailViewHeaderProps,
  LectureDetailViewHeaderStates
> {
  constructor(props: any) {
    super(props);
    this.state = {shareModalVisible: false};
  }
  render() {
    return (
      <View style={this.styles.header}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#FFF" />}
          buttonStyle={{backgroundColor: 'transparent'}}
          onPress={() => {
            this.props.navigation.goBack();
          }}
        />
        <View style={this.styles.buttonWrapper}>
          <Button
            icon={<Icon name="share-outline" size={24} color="#FFF" />}
            buttonStyle={{backgroundColor: 'transparent'}}
            onPress={() => {
              this.setState({
                shareModalVisible: !this.state.shareModalVisible,
              });
            }}
          />

          <View>
            <ShareModal
              text={'클래스를'}
              url={`gajigaksekapp://lecture/${this.props.lecture.lectureId}`}
              modalVisible={this.state.shareModalVisible}
              setModalVisible={(visible: boolean) => {
                this.setState({shareModalVisible: visible});
              }}
              setParentSubZone={(sub: string) => {
                // this.setState({profileSubZone: sub});
              }}
            />
          </View>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    header: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 15,
      position: 'absolute',
      zIndex: 100,
    },
    buttonWrapper: {
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
    },
  });
}
