import ShareModal from '@/screens/modal/ShareModal';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinDetailViewHeaderProps {
  bulletinId: number;
  navigation: any;
  team: any;
}

interface BulletinDetailViewHeaderStates {
  modalVisible: boolean;
}

export default class BulletinDetailViewHeader extends React.Component<
  BulletinDetailViewHeaderProps,
  BulletinDetailViewHeaderStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.container}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => {
              this.props.navigation.goBack();
            }}
          />
          <View style={this.styles.buttonWrapper}>
            <Button
              icon={<Icon name="share-outline" size={24} color="#1d1d1f" />}
              buttonStyle={{backgroundColor: 'white'}}
              onPress={() => {
                this.setState({
                  modalVisible: !this.state.modalVisible,
                });
              }}
            />
            {this.props.team && this.props.team.bulletinsTeam.iamLeader && (
              <Button
                buttonStyle={{backgroundColor: 'white'}}
                titleStyle={{color: '#1d1d1f'}}
                onPress={() => {
                  this.props.navigation.navigate('bulletinEdit', {
                    team: this.props.team,
                  });
                }}
                title={'편집'}
              />
            )}
          </View>
        </View>

        <View>
          <ShareModal
            text={'게시글을'}
            url={`gajigaksekapp://bulletin/${this.props.bulletinId}`}
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 15,
    },
    buttonWrapper: {
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
    },
  });
}
