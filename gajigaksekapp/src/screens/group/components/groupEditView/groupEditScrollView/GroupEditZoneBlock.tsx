import ZoneSelectModal from '@/screens/modal/ZoneSelectModal';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import {Button} from 'react-native-elements';

interface GroupEditZoneBlockProps {
  subZone: string;
  setSubZone: (sub: string) => void;
}

interface GroupEditZoneBlockStates {
  modalVisible: boolean;
}

export default class GroupEditZoneBlock extends React.Component<
  GroupEditZoneBlockProps,
  GroupEditZoneBlockStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.titleContainer}>
          <Text style={this.styles.title}>위치</Text>
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.buttonTitle}
            onPress={() =>
              this.setState({modalVisible: !this.state.modalVisible})
            }
            title={'편집'}
          />
        </View>

        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text}>{this.props.subZone}</Text>
        </View>

        <View>
          <ZoneSelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              this.props.setSubZone(sub);
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    titleContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    title: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    button: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: 'transparent',
      paddingTop: 8,
      paddingBottom: 5,
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    textWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text: {
      width: '70%',
      paddingLeft: 13,
    },
  });
}
