import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {ScrollView} from 'react-native-gesture-handler';
import GoogleMapApiController from '../../../../services/apis/GoogleMapApiController';
import MapView, {Marker} from 'react-native-maps';
import Clipboard from '@react-native-clipboard/clipboard';
import {TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default class ZoneTab extends React.Component<{}, {region: any}> {
  constructor(props: any) {
    super(props);
    this.state = {region: null};
    this.getRegion('서울 시청');
  }

  private async getRegion(address: string) {
    const location = await GoogleMapApiController.getRegion(address);
    this.setState({
      region: {
        latitude: location.lat,
        longitude: location.lng,
        latitudeDelta: 0.005,
        longitudeDelta: 0.005,
      },
    });
    return location;
  }

  private async copyToClipboard(text: string) {
    Clipboard.setString(text);
    const rr = await Clipboard.getString();
    alert('주소가 복사되었습니다.');
  }
  render() {
    return (
      <ScrollView style={{flex: 1, backgroundColor: 'white'}}>
        <View style={this.styles.wrapper1}>
          <Text style={this.styles.text1}>장소</Text>
        </View>
        {this.state.region && (
          <MapView
            style={{width: '100%', height: 200, borderRadius: 10}}
            initialRegion={this.state.region}>
            <Marker coordinate={this.state.region} />
          </MapView>
        )}
        <TouchableOpacity
          style={{flexDirection: 'row', alignItems: 'center', padding: 10}}
          onPress={() => {
            this.copyToClipboard('장소!');
          }}>
          <Icon name="copy-outline" size={15} color="#d2d2d2" />
          <Text style={this.styles.text2}>
            서울시 동작구 상도로 137 3층 (족발야시장 건물)
          </Text>
        </TouchableOpacity>
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    wrapper1: {
      flexDirection: 'row',
      marginTop: 20,
      alignItems: 'center',
      marginBottom: 20,
    },
    text1: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    text2: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginLeft: 6,
    },
  });
}
